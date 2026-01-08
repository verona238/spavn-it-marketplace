package com.spavnit.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для создания нового товара (только для администраторов)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Название товара обязательно")
    private String name;

    @NotBlank(message = "Описание товара обязательно")
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    private BigDecimal price;

    @NotBlank(message = "Категория обязательна")
    private String category; // LIFEHACKS, CHECKLISTS, GAMES, AI_TOOLS, COURSES

    private String imageUrl;

    @NotBlank(message = "Ссылка на скачивание обязательна")
    private String downloadLink;

    private Integer stockQuantity;
}