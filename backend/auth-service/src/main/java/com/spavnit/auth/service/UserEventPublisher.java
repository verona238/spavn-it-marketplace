package com.spavnit.auth.service;

import com.spavnit.auth.config.RabbitMQConfig;
import com.spavnit.auth.dto.UserCreatedEvent;
import com.spavnit.auth.dto.UserDeletedEvent;
import com.spavnit.auth.dto.UserUpdatedEvent;
import com.spavnit.auth.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Сервис для публикации событий пользователя в RabbitMQ
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Публикация события создания пользователя
     */
    public void publishUserCreated(User user) {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EVENTS_EXCHANGE,
                RabbitMQConfig.USER_CREATED_KEY,
                event
        );

        log.info("Событие создания пользователя отправлено: {}", user.getEmail());
    }

    /**
     * Публикация события обновления пользователя
     */
    public void publishUserUpdated(User user) {
        UserUpdatedEvent event = UserUpdatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isActive(user.isActive())
                .isEmailVerified(user.isEmailVerified())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EVENTS_EXCHANGE,
                RabbitMQConfig.USER_UPDATED_KEY,
                event
        );

        log.info("Событие обновления пользователя отправлено: {}", user.getEmail());
    }

    /**
     * Публикация события удаления пользователя
     */
    public void publishUserDeleted(User user) {
        UserDeletedEvent event = UserDeletedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EVENTS_EXCHANGE,
                RabbitMQConfig.USER_DELETED_KEY,
                event
        );

        log.info("Событие удаления пользователя отправлено: {}", user.getEmail());
    }
}