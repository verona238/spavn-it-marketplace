package com.spavnit.order.client;

import com.spavnit.order.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * REST клиент для взаимодействия с Cart Service
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CartClient {

    private final RestTemplate restTemplate;

    @Value("${services.cart-url}")
    private String cartUrl;

    /**
     * Получить корзину пользователя
     */
    public CartResponse getCart(String token) {
        log.info("Запрос корзины из Cart Service");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CartResponse> response = restTemplate.exchange(
                    cartUrl,
                    HttpMethod.GET,
                    entity,
                    CartResponse.class
            );

            log.info("Корзина получена из Cart Service");
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при получении корзины: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить корзину");
        }
    }

    /**
     * Очистить корзину пользователя
     */
    public void clearCart(String token) {
        log.info("Очистка корзины через Cart Service");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    cartUrl,
                    HttpMethod.DELETE,
                    entity,
                    Void.class
            );

            log.info("Корзина очищена");
        } catch (Exception e) {
            log.error("Ошибка при очистке корзины: {}", e.getMessage());
        }
    }
}