package com.spavnit.balance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для корректировки баланса администратором
 * Может использоваться для добавления или вычитания средств
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAdjustmentRequest {

    @NotNull(message = "Сумма обязательна")
    private BigDecimal amount; // Положительное = добавить, отрицательное = вычесть

    @NotBlank(message = "Описание обязательно")
    private String description;
}