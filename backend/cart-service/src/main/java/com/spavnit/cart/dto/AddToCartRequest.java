package com.spavnit.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для добавления товара в корзину
 * Количество всегда равно 1 (цифровые товары)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    @NotNull(message = "ID товара обязателен")
    private Long productId;

    // quantity убрано - всегда = 1
}