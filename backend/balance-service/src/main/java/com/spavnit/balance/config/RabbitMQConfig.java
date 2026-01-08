package com.spavnit.balance.config;

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

    // Имена обменников, очередей и ключей
    public static final String USER_EVENTS_EXCHANGE = "user.events";
    public static final String USER_CREATED_QUEUE = "user.created.balance.queue";
    public static final String USER_CREATED_KEY = "user.created";

    public static final String PAYMENT_EVENTS_EXCHANGE = "payment.events";
    public static final String PAYMENT_RESULT_KEY = "payment.result";

    /**
     * Обменник для событий пользователей
     */
    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(USER_EVENTS_EXCHANGE);
    }

    /**
     * Обменник для событий оплаты
     */
    @Bean
    public TopicExchange paymentEventsExchange() {
        return new TopicExchange(PAYMENT_EVENTS_EXCHANGE);
    }

    /**
     * Очередь для созданных пользователей
     */
    @Bean
    public Queue userCreatedBalanceQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    /**
     * Связываем очередь с обменником
     */
    @Bean
    public Binding userCreatedBalanceBinding() {
        return BindingBuilder
                .bind(userCreatedBalanceQueue())
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
}