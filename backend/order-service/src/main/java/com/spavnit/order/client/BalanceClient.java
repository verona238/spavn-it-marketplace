package com.spavnit.order.client;

import com.spavnit.order.dto.DebitRequest;
import com.spavnit.order.dto.RefundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class BalanceClient {

    private final RestTemplate restTemplate;

    @Value("${services.balance-url}")
    private String balanceUrl;

    /**
     * Списать средства с баланса пользователя
     */
    public void debitBalance(String token, BigDecimal amount, Long orderId) {
        log.info("Списание {} с баланса для заказа {}", amount, orderId);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            DebitRequest request = DebitRequest.builder()
                    .amount(amount)
                    .description("Оплата заказа #" + orderId)
                    .orderId(orderId)
                    .build();

            HttpEntity<DebitRequest> entity = new HttpEntity<>(request, headers);

            restTemplate.exchange(
                    balanceUrl + "/debit",
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

            log.info("Средства списаны для заказа {}", orderId);

        } catch (HttpClientErrorException e) {
            log.error("Ошибка при списании средств для заказа {}: {}", orderId, e.getResponseBodyAsString());
            throw new RuntimeException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Ошибка при списании средств: {}", e.getMessage());
            throw new RuntimeException("Не удалось списать средства с баланса");
        }
    }

    /**
     * Вернуть средства на баланс (при отмене оплаченного заказа)
     */
    public void refundBalance(Long userId, BigDecimal amount, Long orderId) {
        log.info("Возврат {} на баланс пользователя {} для заказа {}", amount, userId, orderId);

        try {
            String url = balanceUrl + "/" + userId + "/refund";

            RefundRequest request = RefundRequest.builder()
                    .amount(amount)
                    .description("Возврат средств за заказ #" + orderId)
                    .orderId(orderId)
                    .build();

            restTemplate.postForObject(url, request, Void.class);

            log.info("Средства возвращены на баланс для заказа {}", orderId);

        } catch (Exception e) {
            log.error("Ошибка при возврате средств для заказа {}: {}", orderId, e.getMessage());
            throw new RuntimeException("Не удалось вернуть средства на баланс");
        }
    }
}