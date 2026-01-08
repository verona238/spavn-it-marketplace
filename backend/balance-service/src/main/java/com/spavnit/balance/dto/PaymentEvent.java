package com.spavnit.balance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Событие результата оплаты для Order/Payment Service
 * Отправляется после попытки списания средств
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private Long orderId;
    private Long userId;
    private String email;
    private BigDecimal amount;
    private String status; // SUCCESS, FAILED
    private String message;
}