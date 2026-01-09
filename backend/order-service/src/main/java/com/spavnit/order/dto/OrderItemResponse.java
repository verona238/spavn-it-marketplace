package com.spavnit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productCategory;
    private String productImageUrl;
    private Integer quantity;
    private String downloadLink;
    private LocalDateTime addedAt;
}