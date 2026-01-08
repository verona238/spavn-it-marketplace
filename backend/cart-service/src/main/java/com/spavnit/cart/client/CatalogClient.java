package com.spavnit.cart.client;

import com.spavnit.cart.dto.ProductInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * REST клиент для взаимодействия с Catalog Service
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogClient {

    private final RestTemplate restTemplate;

    @Value("${services.catalog-url}")
    private String catalogUrl;

    /**
     * Получить информацию о товаре из Catalog Service
     */
    public ProductInfo getProductInfo(Long productId) {
        log.info("Запрос информации о товаре с ID: {} из Catalog Service", productId);

        try {
            String url = catalogUrl + "/products/" + productId;
            ProductInfo product = restTemplate.getForObject(url, ProductInfo.class);

            log.info("Получена информация о товаре: {}", product != null ? product.getName() : "null");
            return product;
        } catch (Exception e) {
            log.error("Ошибка при запросе товара с ID {}: {}", productId, e.getMessage());
            throw new RuntimeException("Не удалось получить информацию о товаре с ID " + productId);
        }
    }

    /**
     * Проверить доступность товара
     */
    public boolean isProductAvailable(Long productId) {
        log.info("Проверка доступности товара с ID: {}", productId);

        try {
            ProductInfo product = getProductInfo(productId);
            return product != null && product.isAvailable();
        } catch (Exception e) {
            log.error("Ошибка при проверке доступности товара с ID {}: {}", productId, e.getMessage());
            return false;
        }
    }
}