package com.spavnit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private String email;
    private BigDecimal totalPrice;
    private String status;
    private String statusDisplayName;
    private Integer itemCount;
    private List<OrderItemResponse> items;
    private String cancellationReason;
    private String cancelledBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;
}