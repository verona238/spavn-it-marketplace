package com.spavnit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Событие создания заказа для Notification Service
 * Отправляется после успешной оплаты для рассылки ссылок на товары
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private String email;
    private BigDecimal totalPrice;
    private List<ProductLinkDto> products;
}