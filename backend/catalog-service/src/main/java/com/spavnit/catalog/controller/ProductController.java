package com.spavnit.catalog.controller;

import com.spavnit.catalog.dto.ProductLinkDto;
import com.spavnit.catalog.dto.ProductResponse;
import com.spavnit.catalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.HashMap;
import java.util.Map;

/**
 * REST контроллер для работы с товарами
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "API для работы с товарами")
public class ProductController {

    private final ProductService productService;

    /**
     * Получение всех товаров
     * GET /api/catalog/products
     */
    @GetMapping
    @Operation(summary = "Получить все товары",
            description = "Получение списка всех товаров")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("GET / - Запрос всех товаров");

        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Получение доступных товаров
     * GET /api/catalog/products/available
     */
    @GetMapping("/available")
    @Operation(summary = "Получить доступные товары",
            description = "Получение списка товаров в наличии")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        log.info("GET /available - Запрос доступных товаров");

        List<ProductResponse> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Получение товара по ID
     * GET /api/catalog/products/{productId}
     */
    @GetMapping("/{productId}")
    @Operation(summary = "Получить товар по ID",
            description = "Получение детальной информации о товаре")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        log.info("GET /{} - Запрос товара", productId);

        ProductResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * Получение товаров по категории
     * GET /api/catalog/products/category/{category}
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Получить товары по категории",
            description = "Получение списка товаров определённой категории")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        log.info("GET /category/{} - Запрос товаров категории", category);

        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Получение ссылки на скачивание товара
     * GET /api/catalog/products/{productId}/download-link
     */
    @GetMapping("/products/{productId}/download-link")
    public ResponseEntity<Map<String, String>> getDownloadLink(@PathVariable Long productId) {
        log.info("GET /products/{}/download-link - Запрос ссылки на скачивание", productId);

        // Найти товар в репозитории
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        Map<String, String> response = new HashMap<>();
        response.put("downloadLink", product.getDownloadLink());

        return ResponseEntity.ok(response);
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Catalog Service is running!");
    }
}