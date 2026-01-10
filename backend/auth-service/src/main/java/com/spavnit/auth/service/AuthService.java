package com.spavnit.auth.service;

import com.spavnit.auth.dto.*;
import com.spavnit.auth.exception.InvalidCredentialsException;
import com.spavnit.auth.exception.UserAlreadyExistsException;
import com.spavnit.auth.model.Role;
import com.spavnit.auth.model.User;
import com.spavnit.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Сервис для обработки аутентификации и авторизации
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final long EXPIRATION_TIME = 86400000; // 24 часа
    private static final int CODE_EXPIRATION_MINUTES = 5;

    /**
     * Регистрация нового пользователя
     */
    public RegisterResponse register(RegisterRequest request) {
        log.info("Регистрация нового пользователя: {}", request.getEmail());

        // Проверка существования пользователя
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        // Создание нового пользователя
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .isActive(true)
                .isEmailVerified(false)
                .twoFactorEnabled(false)
                .build();

        user = userRepository.save(user);

        log.info("Пользователь {} успешно зарегистрирован", user.getEmail());

        // Отправка приветственного письма
        sendWelcomeEmail(user);

        return RegisterResponse.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Регистрация успешна")
                .build();
    }

    /**
     * Вход пользователя с 2FA для администраторов
     */
    public LoginResponse login(LoginRequest request) {
        log.info("Попытка входа пользователя: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Неверный email или пароль"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Неверный email или пароль");
        }

        if (!user.isActive()) {
            throw new InvalidCredentialsException("Аккаунт заблокирован");
        }

        // Если пользователь - администратор, требуется 2FA
        if (user.getRole() == Role.ADMIN) {
            log.info("Пользователь {} является администратором, требуется 2FA", user.getEmail());

            // Генерация и сохранение 2FA кода
            String code = generateTwoFactorCode();
            user.setTwoFactorCode(code);
            user.setTwoFactorExpiry(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));
            userRepository.save(user);

            // Отправка 2FA кода на email
            send2FACode(user, code);

            log.info("Код 2FA отправлен на email: {}", user.getEmail());



            return LoginResponse.builder()
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .requires2FA(true)
                    .message("Для входа требуется подтверждение 2FA. Код отправлен на email.")
                    .build();
        }

        // Для обычных пользователей сразу выдаём токен
        String token = generateToken(user);

        log.info("Пользователь {} успешно вошёл в систему", user.getEmail());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .requires2FA(false)
                .build();
    }

    /**
     * Подтверждение 2FA кода для администраторов
     */
    public LoginResponse verify2FA(TwoFactorRequest request) {
        log.info("Проверка 2FA кода для пользователя: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Пользователь не найден"));

        // Проверка кода
        if (user.getTwoFactorCode() == null || !user.getTwoFactorCode().equals(request.getCode())) {
            throw new InvalidCredentialsException("Неверный код подтверждения");
        }

        // Проверка срока действия кода
        if (user.getTwoFactorExpiry() == null || LocalDateTime.now().isAfter(user.getTwoFactorExpiry())) {
            throw new InvalidCredentialsException("Код подтверждения истёк");
        }

        log.info("2FA код успешно подтверждён для пользователя: {}", user.getEmail());

        // Очистка 2FA данных
        user.setTwoFactorCode(null);
        user.setTwoFactorExpiry(null);
        userRepository.save(user);

        // Генерация JWT токена
        String token = generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .requires2FA(false)
                .build();
    }

    /**
     * Генерация JWT токена с userId и role
     */
    private String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());  // ВАЖНО: добавляем userId в токен!

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Получение ключа для подписи JWT
     */
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Генерация 6-значного кода для 2FA
     */
    private String generateTwoFactorCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Отправка приветственного письма
     */
    private void sendWelcomeEmail(User user) {
        try {
            EmailEvent emailEvent = EmailEvent.builder()
                    .to(user.getEmail())
                    .subject("Добро пожаловать в SpavnIT!")
                    .body("Здравствуйте!\n\n" +
                            "Спасибо за регистрацию в SpavnIT Marketplace.\n\n" +
                            "Ваш аккаунт успешно создан.\n\n" +
                            "С уважением,\n" +
                            "Команда SpavnIT")
                    .type(EmailType.REGISTRATION)
                    .build();

            rabbitTemplate.convertAndSend("notification.exchange", "email.send", emailEvent);
            log.info("Приветственное письмо отправлено для {}", user.getEmail());
        } catch (Exception e) {
            log.error("Ошибка при отправке приветственного письма: {}", e.getMessage());
        }
    }



    /**
     * Отправка 2FA кода на email
     */
    private void send2FACode(User user, String code) {
        try {
            EmailEvent emailEvent = EmailEvent.builder()
                    .to(user.getEmail())
                    .subject("Код подтверждения для входа в SpavnIT")
                    .body("Здравствуйте!\n\n" +
                            "Ваш код подтверждения для входа в систему:\n\n" +
                            code + "\n\n" +
                            "Код действителен в течение " + CODE_EXPIRATION_MINUTES + " минут.\n\n" +
                            "Если это были не вы, проигнорируйте это письмо.\n\n" +
                            "С уважением,\n" +
                            "Команда SpavnIT")
                    .type(EmailType.TWO_FACTOR_AUTH)
                    .build();

            rabbitTemplate.convertAndSend("notification.exchange", "email.send", emailEvent);
            log.info("2FA код отправлен на email: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Ошибка при отправке 2FA кода: {}", e.getMessage());
        }
    }
}