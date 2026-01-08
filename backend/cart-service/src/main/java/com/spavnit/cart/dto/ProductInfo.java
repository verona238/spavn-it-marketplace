package com.spavnit.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для информации о товаре из Catalog Service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    private Long id;
    private String name;
    private BigDecimal price;
    private String category;
    private String categoryDisplayName;
    private String imageUrl;
    private boolean isAvailable;
}