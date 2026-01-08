package com.spavnit.balance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для списания средств с баланса
 * Используется при оплате заказа через Payment Service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebitRequest {

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
    private BigDecimal amount;

    private String description;

    private Long orderId; // ID заказа
}