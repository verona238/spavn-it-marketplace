package com.spavnit.order.client;

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
     * Получить ссылку на скачивание товара
     */
    public String getDownloadLink(Long productId) {
        log.info("Запрос ссылки на скачивание товара {} из Catalog Service", productId);

        try {
            String url = catalogUrl + "/internal/products/" + productId + "/download-link";
            String downloadLink = restTemplate.getForObject(url, String.class);

            log.info("Ссылка на скачивание получена для товара {}", productId);
            return downloadLink;
        } catch (Exception e) {
            log.error("Ошибка при получении ссылки на скачивание для товара {}: {}", productId, e.getMessage());
            return null;
        }
    }
}