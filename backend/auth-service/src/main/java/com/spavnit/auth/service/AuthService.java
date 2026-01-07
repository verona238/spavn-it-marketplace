package com.spavnit.auth.service;

import com.spavnit.auth.config.RabbitMQConfig;
import com.spavnit.auth.dto.*;
import com.spavnit.auth.exception.AuthException;
import com.spavnit.auth.model.Role;
import com.spavnit.auth.model.User;
import com.spavnit.auth.repository.UserRepository;
import com.spavnit.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Сервис авторизации и аутентификации
 * Основная бизнес-логика Auth Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RabbitTemplate rabbitTemplate;
    private final UserEventPublisher userEventPublisher;

    @Value("${two-factor.code-expiration}")
    private int codeExpirationSeconds;

    @Value("${two-factor.code-length}")
    private int codeLength;

    /**
     * Регистрация нового пользователя
     * Требование: "Регистрация пользователей в системе должна быть реализована только по электронной почте"
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Попытка регистрации пользователя: {}", request.getEmail());

        // Проверяем, существует ли уже пользователь
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Пользователь с таким email уже существует");
        }

        // Создаем нового пользователя
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT) // По умолчанию все новые пользователи - клиенты
                .isActive(true)
                .isEmailVerified(false)
                .twoFactorEnabled(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Пользователь успешно зарегистрирован: {}", savedUser.getEmail());

        userEventPublisher.publishUserCreated(savedUser);

        // Отправляем приветственное письмо
        sendWelcomeEmail(savedUser.getEmail());

        // Генерируем токены
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .requiresTwoFactor(false)
                .message("Регистрация успешна")
                .build();
    }

    /**
     * Вход в систему
     * Требование: "В системе должна быть реализована двухфакторная аутентификация по электронной почте
     * для пользователей с ролью Администратор"
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Попытка входа пользователя: {}", request.getEmail());

        // Находим пользователя
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Неверный email или пароль"));

        // Проверяем пароль
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Неверный email или пароль");
        }

        // КРИТИЧЕСКАЯ ПРОВЕРКА: Деактивированные пользователи не могут войти
        if (!user.isActive()) {
            log.warn("Попытка входа деактивированного пользователя: {}", user.getEmail());
            throw new AuthException("Ваш аккаунт был деактивирован. Обратитесь к администратору.");
        }

        // Если пользователь - администратор, требуем 2FA
        if (user.getRole() == Role.ADMIN) {
            log.info("Пользователь {} является администратором, требуется 2FA", user.getEmail());

            // Генерируем код 2FA
            String code = generateTwoFactorCode();

            // Сохраняем код и время истечения
            user.setTwoFactorCode(passwordEncoder.encode(code));
            user.setTwoFactorExpiry(LocalDateTime.now().plusSeconds(codeExpirationSeconds));
            userRepository.save(user);

            // Отправляем код на email
            sendTwoFactorCode(user.getEmail(), code);

            return AuthResponse.builder()
                    .requiresTwoFactor(true)
                    .email(user.getEmail())
                    .message("Код двухфакторной аутентификации отправлен на email")
                    .build();
        }

        // Для обычных пользователей сразу выдаем токены
        log.info("Пользователь {} успешно авторизован", user.getEmail());
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .requiresTwoFactor(false)
                .message("Вход выполнен успешно")
                .build();
    }

    /**
     * Подтверждение 2FA кода
     * Требование: "В системе должна быть реализована двухфакторная аутентификация по электронной почте"
     */
    @Transactional
    public AuthResponse verifyTwoFactor(TwoFactorRequest request) {
        log.info("Проверка 2FA кода для пользователя: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));

        // ПРОВЕРКА: Деактивированные пользователи не могут пройти 2FA
        if (!user.isActive()) {
            log.warn("Попытка 2FA деактивированного пользователя: {}", user.getEmail());
            throw new AuthException("Ваш аккаунт был деактивирован. Обратитесь к администратору.");
        }

        // Проверяем, что код был отправлен
        if (user.getTwoFactorCode() == null || user.getTwoFactorExpiry() == null) {
            throw new AuthException("Код 2FA не был запрошен");
        }

        // Проверяем, не истек ли код
        if (LocalDateTime.now().isAfter(user.getTwoFactorExpiry())) {
            throw new AuthException("Код 2FA истек. Пожалуйста, войдите снова");
        }

        // Проверяем правильность кода
        if (!passwordEncoder.matches(request.getCode(), user.getTwoFactorCode())) {
            throw new AuthException("Неверный код 2FA");
        }

        // Очищаем код 2FA
        user.setTwoFactorCode(null);
        user.setTwoFactorExpiry(null);
        userRepository.save(user);

        log.info("2FA код успешно подтвержден для пользователя: {}", user.getEmail());

        // Генерируем токены
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .requiresTwoFactor(false)
                .message("Двухфакторная аутентификация пройдена успешно")
                .build();
    }

    /**
     * Генерация кода 2FA
     */
    private String generateTwoFactorCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }


    /**
     * Отправка кода 2FA на email через RabbitMQ
     */
    private void sendTwoFactorCode(String email, String code) {
        EmailEvent emailEvent = EmailEvent.builder()
                .to(email)
                .subject("Код двухфакторной аутентификации")
                .body("Ваш код подтверждения: " + code + "\nКод действителен в течение "
                        + (codeExpirationSeconds / 60) + " минут.")
                .type(EmailType.TWO_FACTOR_CODE)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                emailEvent
        );

        log.info("Код 2FA отправлен на email: {}", email);
    }

    /**
     * Отправка приветственного письма
     */
    private void sendWelcomeEmail(String email) {
        EmailEvent emailEvent = EmailEvent.builder()
                .to(email)
                .subject("Добро пожаловать в SpavnIT!")
                .body("Здравствуйте!\n\nСпасибо за регистрацию в интернет-магазине SpavnIT.\n\n"
                        + "Теперь вы можете просматривать наш каталог и делать заказы.\n\n"
                        + "С уважением,\nКоманда SpavnIT")
                .type(EmailType.WELCOME)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                emailEvent
        );

        log.info("Приветственное письмо отправлено на: {}", email);
    }
}