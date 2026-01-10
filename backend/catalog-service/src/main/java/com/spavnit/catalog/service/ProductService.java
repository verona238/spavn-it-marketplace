package com.spavnit.catalog.service;

import com.spavnit.catalog.dto.ProductResponse;
import com.spavnit.catalog.exception.ProductNotFoundException;
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
public class ProductService {

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

        return productRepository.findByAvailableTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение товара по ID
     */
    public ProductResponse getProductById(Long id) {
        log.info("Получение товара {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден"));

        return mapToResponse(product);
    }

    /**
     * Получение товаров по категории
     */
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Получение товаров категории: {}", category);

        return productRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение ссылки на скачивание товара
     */
    public String getDownloadLink(Long productId) {
        log.info("Получение ссылки для товара {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + productId + " не найден"));

        return product.getDownloadLink();
    }

    /**
     * Маппинг Product в ProductResponse
     */
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory().name())
                .categoryDisplayName(product.getCategory().getDisplayName())
                .imageUrl(product.getImageUrl())
                .downloadLink(product.getDownloadLink())
                .available(product.isAvailable())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}