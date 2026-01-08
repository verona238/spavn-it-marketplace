package com.spavnit.catalog.controller;

import com.spavnit.catalog.dto.*;
import com.spavnit.catalog.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления каталогом товаров
 * Все эндпоинты доступны по адресу: http://localhost:8085/api/catalog
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Catalog Management", description = "API для управления каталогом товаров и услуг \"Спавн в IT\"")
public class CatalogController {

    private final CatalogService catalogService;

    /**
     * Получение всех товаров (публичный доступ)
     * GET /api/catalog/products
     */
    @GetMapping("/products")
    @Operation(summary = "Получить все товары",
            description = "Получение полного списка товаров. Публичный доступ")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("GET /products - Запрос всех товаров");
        List<ProductResponse> products = catalogService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Получение товара по ID (публичный доступ)
     * GET /api/catalog/products/{id}
     */
    @GetMapping("/products/{id}")
    @Operation(summary = "Получить товар по ID",
            description = "Получение детальной информации о товаре. Публичный доступ")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("GET /products/{} - Запрос товара с ID: {}", id, id);
        ProductResponse product = catalogService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Поиск товаров по названию (публичный доступ)
     * GET /api/catalog/products/search?query=...
     */
    @GetMapping("/products/search")
    @Operation(summary = "Поиск товаров",
            description = "Поиск товаров по названию. Публичный доступ")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String query) {
        log.info("GET /products/search?query={} - Поиск товаров", query);
        List<ProductResponse> products = catalogService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    /**
     * Фильтрация по категории (публичный доступ)
     * GET /api/catalog/products/category/{category}
     */
    @GetMapping("/products/category/{category}")
    @Operation(summary = "Фильтр по категории",
            description = "Получение товаров определенной категории. Публичный доступ")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        log.info("GET /products/category/{} - Фильтрация по категории: {}", category, category);
        List<ProductResponse> products = catalogService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Получение списка категорий (публичный доступ)
     * GET /api/catalog/categories
     */
    @GetMapping("/categories")
    @Operation(summary = "Получить список категорий",
            description = "Получение всех доступных категорий товаров. Публичный доступ")
    public ResponseEntity<List<CategoryInfo>> getAllCategories() {
        log.info("GET /categories - Запрос списка категорий");
        List<CategoryInfo> categories = catalogService.getAllCategories();
        return ResponseEntity.ok(categories);
    }



    /**
     * Создание товара (только для администраторов)
     * POST /api/catalog/admin/products
     */
    @PostMapping("/admin/products")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Создать товар (ADMIN)",
            description = "Создание нового товара. Доступно только администраторам")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("POST /admin/products - Создание товара: {}", request.getName());
        ProductResponse product = catalogService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * Обновление товара (только для администраторов)
     * PUT /api/catalog/admin/products/{id}
     */
    @PutMapping("/admin/products/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Обновить товар (ADMIN)",
            description = "Обновление существующего товара. Доступно только администраторам")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        log.info("PUT /admin/products/{} - Обновление товара с ID: {}", id, id);
        ProductResponse product = catalogService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    /**
     * Удаление товара (только для администраторов)
     * DELETE /api/catalog/admin/products/{id}
     */
    @DeleteMapping("/admin/products/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Удалить товар (ADMIN)",
            description = "Удаление товара. Доступно только администраторам")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /admin/products/{} - Удаление товара с ID: {}", id, id);
        catalogService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение ссылки на скачивание товара
     * GET /api/catalog/internal/products/{id}/download-link
     * Используется Order Service после оплаты (внутренний эндпоинт)
     */
    @GetMapping("/internal/products/{id}/download-link")
    @Operation(summary = "Получить ссылку на скачивание (INTERNAL)",
            description = "Получение ссылки на скачивание товара. Используется другими сервисами")
    public ResponseEntity<String> getDownloadLink(@PathVariable Long id) {
        log.info("GET /internal/products/{}/download-link - Запрос ссылки на скачивание", id);
        String downloadLink = catalogService.getDownloadLink(id);
        return ResponseEntity.ok(downloadLink);
    }

    /**
     * Проверка доступности товара (внутренний эндпоинт)
     * GET /api/catalog/internal/products/{id}/available
     */
    @GetMapping("/internal/products/{id}/available")
    @Operation(summary = "Проверить доступность товара (INTERNAL)",
            description = "Проверка доступности товара. Используется другими сервисами")
    public ResponseEntity<Boolean> isProductAvailable(@PathVariable Long id) {
        log.info("GET /internal/products/{}/available - Проверка доступности товара", id);
        boolean available = catalogService.isProductAvailable(id);
        return ResponseEntity.ok(available);
    }

    /**
     * Health check эндпоинт
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Catalog Service is running!");
    }
}