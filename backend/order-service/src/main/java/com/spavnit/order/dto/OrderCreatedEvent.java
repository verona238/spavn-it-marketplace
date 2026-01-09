package com.spavnit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private String email;
    private BigDecimal totalPrice;
    private Integer itemCount;
}