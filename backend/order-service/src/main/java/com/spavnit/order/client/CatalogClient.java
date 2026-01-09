package com.spavnit.order.client;

import com.spavnit.order.dto.ProductLinkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        log.info("Получение ссылки на скачивание товара {}", productId);

        try {
            String url = catalogUrl + "/products/" + productId + "/download-link";

            ProductLinkDto response = restTemplate.getForObject(url, ProductLinkDto.class);

            if (response != null && response.getDownloadLink() != null) {
                log.info("Ссылка получена для товара {}", productId);
                return response.getDownloadLink();
            }

            log.warn("Ссылка не найдена для товара {}", productId);
            return null;

        } catch (Exception e) {
            log.error("Ошибка при получении ссылки для товара {}: {}", productId, e.getMessage());
            return null;
        }
    }
}