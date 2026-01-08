package com.spavnit.catalog.service;

import com.spavnit.catalog.dto.*;
import com.spavnit.catalog.exception.InvalidCategoryException;
import com.spavnit.catalog.exception.ProductNotFoundException;
import com.spavnit.catalog.model.Category;
import com.spavnit.catalog.model.Product;
import com.spavnit.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления каталогом товаров
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final ProductRepository productRepository;

    /**
     * Получение всех товаров (публичный доступ)
     */
    public List<ProductResponse> getAllProducts() {
        log.info("Получение всех товаров");

        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение только доступных товаров (публичный доступ)
     */
    public List<ProductResponse> getAvailableProducts() {
        log.info("Получение доступных товаров");

        return productRepository.findByIsAvailableTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение товара по ID (публичный доступ)
     */
    public ProductResponse getProductById(Long id) {
        log.info("Получение товара с ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден"));

        return mapToResponse(product);
    }

    /**
     * Поиск товаров по названию (публичный доступ)
     * Требование: фильтрация товаров в каталоге по названию (поиск)
     */
    public List<ProductResponse> searchProducts(String query) {
        log.info("Поиск товаров по запросу: {}", query);

        if (query == null || query.trim().isEmpty()) {
            return getAvailableProducts();
        }

        return productRepository.findByNameContainingIgnoreCaseAndIsAvailableTrue(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Фильтрация товаров по категории (публичный доступ)
     * Требование: фильтрация товаров по доступным категориям
     */
    public List<ProductResponse> getProductsByCategory(String categoryName) {
        log.info("Получение товаров категории: {}", categoryName);

        Category category = parseCategory(categoryName);

        return productRepository.findByCategoryAndIsAvailableTrue(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение списка всех категорий (публичный доступ)
     */
    public List<CategoryInfo> getAllCategories() {
        log.info("Получение списка категорий");

        return Arrays.stream(Category.values())
                .map(category -> CategoryInfo.builder()
                        .name(category.name())
                        .displayName(category.getDisplayName())
                        .description(category.getDescription())
                        .productCount(productRepository.countByCategory(category))
                        .build())
                .collect(Collectors.toList());
    }



    /**
     * Создание товара (только для администраторов)
     * Требование: CRUD-операции с товарами для администраторов
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Администратор создает товар: {}", request.getName());

        Category category = parseCategory(request.getCategory());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .imageUrl(request.getImageUrl())
                .downloadLink(request.getDownloadLink())
                .isAvailable(true)
                .stockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 999)
                .build();

        product = productRepository.save(product);

        log.info("Товар создан с ID: {}", product.getId());

        return mapToResponse(product);
    }

    /**
     * Обновление товара (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Администратор обновляет товар с ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден"));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getCategory() != null) {
            product.setCategory(parseCategory(request.getCategory()));
        }
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getDownloadLink() != null) {
            product.setDownloadLink(request.getDownloadLink());
        }
        if (request.getIsAvailable() != null) {
            product.setAvailable(request.getIsAvailable());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }

        product = productRepository.save(product);

        log.info("Товар обновлен: {}", product.getName());

        return mapToResponse(product);
    }

    /**
     * Удаление товара (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Администратор удаляет товар с ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + id + " не найден"));

        productRepository.delete(product);

        log.info("Товар удален: {}", product.getName());
    }

    /**
     * Получение ссылки на скачивание товара
     * Используется Order Service после оплаты
     */
    public String getDownloadLink(Long productId) {
        log.info("Запрос ссылки на скачивание товара с ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + productId + " не найден"));

        return product.getDownloadLink();
    }

    /**
     * Проверка доступности товара
     */
    public boolean isProductAvailable(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с ID " + productId + " не найден"));

        return product.isAvailable() && product.getStockQuantity() > 0;
    }



    /**
     * Парсинг категории из строки
     */
    private Category parseCategory(String categoryName) {
        try {
            return Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException("Некорректная категория: " + categoryName +
                    ". Доступные категории: " + Arrays.toString(Category.values()));
        }
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
                .isAvailable(product.isAvailable())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}