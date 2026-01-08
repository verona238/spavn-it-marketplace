package com.spavnit.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о товаре в корзине
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productCategory;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal totalPrice; // цена * количество
    private LocalDateTime addedAt;
}