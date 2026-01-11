package com.spavnit.catalog.service;

import com.spavnit.catalog.dto.ProductResponse;
import com.spavnit.catalog.model.Product;
import com.spavnit.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final ProductRepository productRepository;

    /**
     * Получение всех товаров
     */
    public List<ProductResponse> getAllProducts() {
        log.info("Получение всех товаров");
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение доступных товаров
     */
    public List<ProductResponse> getAvailableProducts() {
        log.info("Получение доступных товаров");
        return productRepository.findByIsAvailableTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение товара по ID
     */
    public ProductResponse getProductById(Long id) {
        log.info("Получение товара с ID {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар с ID " + id + " не найден"));
        return mapToResponse(product);
    }

    /**
     * Маппинг Product в ProductResponse
     */
    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory().name());
        response.setCategoryDisplayName(product.getCategory().getDisplayName());
        response.setImageUrl(product.getImageUrl());
        response.setDownloadLink(product.getDownloadLink());
        response.setAvailable(product.isAvailable());
        response.setStockQuantity(product.getStockQuantity());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}