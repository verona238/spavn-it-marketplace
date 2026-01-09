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
public class OrderCancelledEvent {
    private Long orderId;
    private Long userId;
    private String email;
    private String cancellationReason;
    private String cancelledBy;
    private boolean refunded;
    private BigDecimal refundedAmount;
}