package com.spavnit.user.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ
 */
@Configuration
public class RabbitMQConfig {

    // Имена обменников и очередей
    public static final String USER_EVENTS_EXCHANGE = "user.events";
    public static final String USER_CREATED_QUEUE = "user.created.queue";
    public static final String USER_CREATED_KEY = "user.created";

    public static final String AUTH_EVENTS_EXCHANGE = "auth.events";
    public static final String ROLE_CHANGED_KEY = "role.changed";

    /**
     * Обменник для событий пользователей
     */
    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(USER_EVENTS_EXCHANGE);
    }

    /**
     * Обменник для событий авторизации
     */
    @Bean
    public TopicExchange authEventsExchange() {
        return new TopicExchange(AUTH_EVENTS_EXCHANGE);
    }

    /**
     * Очередь для созданных пользователей
     */
    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
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

    // Дополнительные очереди для синхронизации
    public static final String USER_UPDATED_QUEUE = "user.updated.queue";
    public static final String USER_UPDATED_KEY = "user.updated";

    public static final String USER_DELETED_QUEUE = "user.deleted.queue";
    public static final String USER_DELETED_KEY = "user.deleted";

    /**
     * Очередь для обновленных пользователей
     */
    @Bean
    public Queue userUpdatedQueue() {
        return new Queue(USER_UPDATED_QUEUE, true);
    }

    /**
     * Очередь для удаленных пользователей
     */
    @Bean
    public Queue userDeletedQueue() {
        return new Queue(USER_DELETED_QUEUE, true);
    }

    /**
     * Связываем очередь обновлений с обменником
     */
    @Bean
    public Binding userUpdatedBinding() {
        return BindingBuilder
                .bind(userUpdatedQueue())
                .to(userEventsExchange())
                .with(USER_UPDATED_KEY);
    }

    /**
     * Связываем очередь удалений с обменником
     */
    @Bean
    public Binding userDeletedBinding() {
        return BindingBuilder
                .bind(userDeletedQueue())
                .to(userEventsExchange())
                .with(USER_DELETED_KEY);
    }
}