package com.spavnit.catalog.controller;

import com.spavnit.catalog.dto.ProductResponse;
import com.spavnit.catalog.model.Product;
import com.spavnit.catalog.repository.ProductRepository;
import com.spavnit.catalog.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Catalog", description = "API для работы с каталогом товаров")
public class CatalogController {

    private final CatalogService catalogService;
    private final ProductRepository productRepository;

    /**
     * Получение всех товаров
     */
    @GetMapping
    @Operation(summary = "Получить все товары")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("GET /products - Получение всех товаров");
        return ResponseEntity.ok(catalogService.getAllProducts());
    }

    /**
     * Получение доступных товаров
     */
    @GetMapping("/available")
    @Operation(summary = "Получить доступные товары")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        log.info("GET /products/available - Получение доступных товаров");
        return ResponseEntity.ok(catalogService.getAvailableProducts());
    }

    /**
     * Получение товара по ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("GET /products/{} - Получение товара", id);
        return ResponseEntity.ok(catalogService.getProductById(id));
    }

    /**
     * Получение ссылки на скачивание товара
     */
    @GetMapping("/{productId}/download-link")
    @Operation(summary = "Получить ссылку на скачивание")
    public ResponseEntity<Map<String, String>> getDownloadLink(@PathVariable Long productId) {
        log.info("GET /products/{}/download-link - Запрос ссылки", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар с ID " + productId + " не найден"));

        Map<String, String> response = new HashMap<>();
        response.put("downloadLink", product.getDownloadLink());

        return ResponseEntity.ok(response);
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Catalog Service is running!");
    }
}