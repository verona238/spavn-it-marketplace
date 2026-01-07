package com.spavnit.user.service;

import com.spavnit.user.config.RabbitMQConfig;
import com.spavnit.user.dto.RoleChangedEvent;
import com.spavnit.user.dto.UserDeletedEvent;
import com.spavnit.user.dto.UserUpdatedEvent;
import com.spavnit.user.model.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Сервис для публикации событий в RabbitMQ
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Публикация полного события обновления пользователя
     * Используется для синхронизации ВСЕХ изменений
     */
    public void publishUserUpdated(UserProfile profile) {
        UserUpdatedEvent event = UserUpdatedEvent.builder()
                .userId(profile.getId())
                .email(profile.getEmail())
                .role(profile.getRole().name())
                .isActive(profile.isActive())
                .isEmailVerified(profile.isEmailVerified())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EVENTS_EXCHANGE,
                RabbitMQConfig.USER_UPDATED_KEY,
                event
        );

        log.info("Событие обновления пользователя отправлено в Auth Service для: {}", profile.getEmail());
    }

    /**
     * Публикация события изменения роли (legacy - для обратной совместимости)
     */
    public void publishRoleChanged(UserProfile profile) {
        // Теперь просто вызываем publishUserUpdated для полной синхронизации
        publishUserUpdated(profile);
    }

    /**
     * Публикация события деактивации пользователя
     */
    public void publishUserDeactivated(UserProfile profile) {
        // Используем общее событие обновления
        publishUserUpdated(profile);
    }
}