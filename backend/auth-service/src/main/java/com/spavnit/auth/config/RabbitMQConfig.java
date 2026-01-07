package com.spavnit.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ
 * Создаем очереди и обменники для обмена сообщениями между сервисами
 */
@Configuration
public class RabbitMQConfig {

    // Имена обменников
    public static final String USER_EVENTS_EXCHANGE = "user.events";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    // Имена очередей
    public static final String USER_CREATED_QUEUE = "user.created.queue";
    public static final String EMAIL_QUEUE = "email.queue";

    // Routing keys
    public static final String USER_CREATED_KEY = "user.created";
    public static final String EMAIL_ROUTING_KEY = "email.send";

    /**
     * Обменник для событий пользователей
     */
    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(USER_EVENTS_EXCHANGE);
    }

    /**
     * Обменник для уведомлений
     */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    /**
     * Очередь для созданных пользователей
     */
    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    /**
     * Очередь для email
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    /**
     * Связываем очередь с обменником
     */
    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(userEventsExchange())
                .with(USER_CREATED_KEY);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(notificationExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    /**
     * Конвертер сообщений в JSON
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate для отправки сообщений
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    // Для синхронизации ролей
    public static final String AUTH_EVENTS_EXCHANGE = "auth.events";
    public static final String ROLE_CHANGED_QUEUE = "role.changed.queue";
    public static final String ROLE_CHANGED_KEY = "role.changed";

    @Bean
    public TopicExchange authEventsExchange() {
        return new TopicExchange(AUTH_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue roleChangedQueue() {
        return new Queue(ROLE_CHANGED_QUEUE, true);
    }

    @Bean
    public Binding roleChangedBinding() {
        return BindingBuilder
                .bind(roleChangedQueue())
                .to(authEventsExchange())
                .with(ROLE_CHANGED_KEY);
    }
    // Дополнительные события для синхронизации
    public static final String USER_DELETED_KEY = "user.deleted";

    // Для получения событий обновления из User Service
    public static final String USER_UPDATED_AUTH_QUEUE = "user.updated.auth.queue";
    public static final String USER_UPDATED_KEY = "user.updated";

    @Bean
    public Queue userUpdatedAuthQueue() {
        return new Queue(USER_UPDATED_AUTH_QUEUE, true);
    }

    @Bean
    public Binding userUpdatedAuthBinding() {
        return BindingBuilder
                .bind(userUpdatedAuthQueue())
                .to(userEventsExchange())
                .with(USER_UPDATED_KEY);
    }
}