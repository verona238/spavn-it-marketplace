package com.spavnit.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productCategory;
    private String productImageUrl;
    private Integer quantity;
    }