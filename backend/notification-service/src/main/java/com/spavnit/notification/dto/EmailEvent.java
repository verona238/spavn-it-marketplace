package com.spavnit.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для событий email из RabbitMQ
 * Должно совпадать с EmailEvent в Auth Service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailEvent {
    private String to;
    private String subject;
    private String body;
    private EmailType type;
}