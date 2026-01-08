package com.spavnit.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для информации о категории
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfo {
    private String name;
    private String displayName;
    private String description;
    private Long productCount;
}