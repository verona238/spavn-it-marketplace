package com.spavnit.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private Long userId;
    private String email;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
    private Integer itemCount;
}