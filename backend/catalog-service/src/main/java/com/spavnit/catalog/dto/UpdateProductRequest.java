package com.spavnit.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для обновления товара (только для администраторов)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private String name;

    private String description;

    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    private BigDecimal price;

    private String category;

    private String imageUrl;

    private String downloadLink;

    private Boolean isAvailable;

    private Integer stockQuantity;
}