package com.spavnit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для списания средств из Balance Service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebitRequest {
    private BigDecimal amount;
    private String description;
    private Long orderId;
}