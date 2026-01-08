package com.spavnit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для информации о товаре с ссылкой на скачивание
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLinkDto {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String downloadLink;
}