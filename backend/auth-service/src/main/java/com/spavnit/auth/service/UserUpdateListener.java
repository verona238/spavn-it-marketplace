package com.spavnit.auth.service;

import com.spavnit.auth.dto.UserUpdatedEvent;
import com.spavnit.auth.model.Role;
import com.spavnit.auth.model.User;
import com.spavnit.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener для обработки событий обновления пользователя из User Service
 * Синхронизирует ВСЕ изменения: роль, статус активности, верификацию email
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserUpdateListener {

    private final UserRepository userRepository;

    @RabbitListener(queues = "user.updated.auth.queue")
    @Transactional
    public void handleUserUpdatedEvent(UserUpdatedEvent event) {
        log.info("Получено событие обновления пользователя: {}", event.getEmail());

        User user = userRepository.findByEmail(event.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + event.getEmail()));

        // Синхронизируем ВСЕ поля
        Role oldRole = user.getRole();
        boolean oldActive = user.isActive();
        boolean oldVerified = user.isEmailVerified();

        user.setRole(Role.valueOf(event.getRole()));
        user.setActive(event.isActive());
        user.setEmailVerified(event.isEmailVerified());

        userRepository.save(user);

        // Логируем все изменения
        if (!oldRole.equals(user.getRole())) {
            log.info("Роль изменена: {} → {}", oldRole, user.getRole());
        }
        if (oldActive != user.isActive()) {
            log.info("Статус активности изменен: {} → {}", oldActive, user.isActive());
        }
        if (oldVerified != user.isEmailVerified()) {
            log.info("Статус верификации email изменен: {} → {}", oldVerified, user.isEmailVerified());
        }

        log.info("Пользователь {} полностью синхронизирован в Auth Service", event.getEmail());
    }
}