package com.spavnit.order.service;

import com.spavnit.order.client.BalanceClient;
import com.spavnit.order.client.CartClient;
import com.spavnit.order.client.CatalogClient;
import com.spavnit.order.config.RabbitMQConfig;
import com.spavnit.order.dto.*;
import com.spavnit.order.exception.EmptyCartException;
import com.spavnit.order.exception.InvalidOrderStatusException;
import com.spavnit.order.exception.OrderNotFoundException;
import com.spavnit.order.exception.PaymentFailedException;
import com.spavnit.order.model.Order;
import com.spavnit.order.model.OrderItem;
import com.spavnit.order.model.OrderStatus;
import com.spavnit.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final CatalogClient catalogClient;
    private final BalanceClient balanceClient;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Создание заказа из корзины
     */
    @Transactional
    public OrderResponse createOrder(String email, Long userId, String token) {
        log.info("Создание заказа для пользователя: {}", email);

        // 1. Получаем корзину
        CartResponse cart = cartClient.getCart(token);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException("Корзина пуста");
        }

        log.info("Найдено {} товаров в корзине на сумму {}", cart.getItemCount(), cart.getTotalPrice());

        // 2. Создаём заказ
        Order order = Order.builder()
                .userId(userId)
                .email(email)
                .totalPrice(cart.getTotalPrice())
                .status(OrderStatus.CREATED)
                .build();

        // 3. Добавляем товары из корзины в заказ
        for (CartItemDto cartItem : cart.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .productId(cartItem.getProductId())
                    .productName(cartItem.getProductName())
                    .productPrice(cartItem.getProductPrice())
                    .productCategory(cartItem.getProductCategory())
                    .productImageUrl(cartItem.getProductImageUrl())
                    .quantity(cartItem.getQuantity())
                    .build();

            order.addItem(orderItem);
        }

        order = orderRepository.save(order);
        log.info("Заказ {} создан на сумму {}", order.getId(), order.getTotalPrice());

        // 4. Отправляем событие о создании заказа
        publishOrderCreatedEvent(order);

        return mapToResponse(order);
    }

    /**
     * Оплата заказа
     */
    @Transactional
    public OrderResponse payOrder(String email, Long orderId, String token) {
        log.info("Оплата заказа {} пользователем: {}", orderId, email);

        // 1. Находим заказ
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заказ с ID " + orderId + " не найден"));

        // 2. Проверяем владельца
        if (!order.getEmail().equals(email)) {
            throw new OrderNotFoundException("Заказ с ID " + orderId + " не найден");
        }

        // 3. Проверяем статус
        if (order.getStatus() == OrderStatus.PAID) {
            throw new InvalidOrderStatusException("Заказ уже оплачен");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("Нельзя оплатить отменённый заказ");
        }



        // 4. Списываем средства с баланса
        try {
            balanceClient.debitBalance(token, order.getTotalPrice(), order.getId());
            log.info("Средства списаны для заказа {}", orderId);
        } catch (Exception e) {
            log.error("Ошибка при списании средств для заказа {}: {}", orderId, e.getMessage());
            throw new PaymentFailedException(e.getMessage());
        }

        // 5. Обновляем статус заказа на PAID
        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
        order = orderRepository.save(order);
        log.info("Заказ {} оплачен", orderId);

        // 6. Получаем ссылки на скачивание товаров
        for (OrderItem item : order.getItems()) {
            String downloadLink = catalogClient.getDownloadLink(item.getProductId());
            item.setDownloadLink(downloadLink);
        }
        order = orderRepository.save(order);

        // 7. Отправляем событие для рассылки ссылок на товары
        publishOrderPaidEvent(order);

        // 8. Очищаем корзину ПОСЛЕ успешной оплаты
        try {
            cartClient.clearCart(token);
            log.info("Корзина очищена после оплаты заказа {}", orderId);
        } catch (Exception e) {
            log.error("Не удалось очистить корзину: {}", e.getMessage());
            // Не бросаем исключение, так как оплата уже прошла
        }

        return mapToResponse(order);
    }

    /**
     * ОТМЕНА ЗАКАЗА (только для администраторов)
     *
     * Функциональные требования:
     * 1. Отмена из CREATED: средства НЕ возвращаются (не были списаны)
     * 2. Отмена из PAID: средства ВОЗВРАЩАЮТСЯ на баланс
     * 3. Отмена из CANCELLED: ошибка
     * 4. Причина отмены обязательна
     * 5. Логирование всех действий
     * 6. Email-уведомление пользователю
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse cancelOrder(String adminEmail, Long orderId, CancelOrderRequest request) {
        log.info("Администратор {} отменяет заказ {}", adminEmail, orderId);
        log.info("Причина отмены: {}", request.getReason());

        // 1. Находим заказ
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заказ с ID " + orderId + " не найден"));

        // 2. Проверяем, что заказ не отменён
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("Заказ уже отменён");
        }

        boolean wasRefunded = false;

        // 3. Если заказ был ОПЛАЧЕН - возвращаем средства
        if (order.getStatus() == OrderStatus.PAID) {
            log.info("Заказ {} был оплачен, возвращаем {} на баланс пользователя {}",
                    orderId, order.getTotalPrice(), order.getUserId());

            try {
                balanceClient.refundBalance(order.getUserId(), order.getTotalPrice(), order.getId());
                log.info("Средства возвращены на баланс для заказа {}", orderId);
                wasRefunded = true;
            } catch (Exception e) {
                log.error("Ошибка при возврате средств для заказа {}: {}", orderId, e.getMessage());
                throw new RuntimeException("Не удалось вернуть средства на баланс");
            }
        } else {
            log.info("Заказ {} не был оплачен, возврат средств не требуется", orderId);
        }

        // 4. Обновляем статус заказа на CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(request.getReason());
        order.setCancelledBy(adminEmail);
        order.setCancelledAt(LocalDateTime.now());
        order = orderRepository.save(order);

        log.info("Заказ {} отменён администратором {}", orderId, adminEmail);

        // 5. Отправляем событие об отмене заказа для email-уведомления
        publishOrderCancelledEvent(order, wasRefunded);

        return mapToResponse(order);
    }



    /**
     * Получение заказа по ID
     */
    public OrderResponse getOrderById(Long orderId, String email) {
        log.info("Получение заказа {} для пользователя {}", orderId, email);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заказ с ID " + orderId + " не найден"));

        // Проверяем, что заказ принадлежит пользователю
        if (!order.getEmail().equals(email)) {
            throw new OrderNotFoundException("Заказ с ID " + orderId + " не найден");
        }

        return mapToResponse(order);
    }

    /**
     * Получение всех заказов пользователя
     */
    public List<OrderResponse> getUserOrders(String email) {
        log.info("Получение всех заказов пользователя: {}", email);

        return orderRepository.findByEmailOrderByCreatedAtDesc(email).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение всех заказов (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrders() {
        log.info("Администратор запросил список всех заказов");

        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Публикация события о создании заказа
     */
    private void publishOrderCreatedEvent(Order order) {
        log.info("Отправка события создания заказа {}", order.getId());

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .email(order.getEmail())
                .totalPrice(order.getTotalPrice())
                .itemCount(order.getItems().size())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_KEY,
                event
        );

        log.info("Событие создания заказа отправлено");
    }

    /**
     * Публикация события об оплате заказа (с ссылками на товары)
     */
    private void publishOrderPaidEvent(Order order) {
        log.info("Отправка события оплаты заказа {} со ссылками на товары", order.getId());

        List<ProductLinkInfo> productLinks = order.getItems().stream()
                .map(item -> ProductLinkInfo.builder()
                        .productName(item.getProductName())
                        .downloadLink(item.getDownloadLink())
                        .build())
                .collect(Collectors.toList());

        OrderPaidEvent event = OrderPaidEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .email(order.getEmail())
                .totalPrice(order.getTotalPrice())
                .productLinks(productLinks)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_PAID_KEY,
                event
        );

        log.info("Событие оплаты заказа отправлено");
    }

    /**
     * Публикация события об отмене заказа
     */
    private void publishOrderCancelledEvent(Order order, boolean wasRefunded) {
        log.info("Отправка события отмены заказа {}", order.getId());

        OrderCancelledEvent event = OrderCancelledEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .email(order.getEmail())
                .cancellationReason(order.getCancellationReason())
                .cancelledBy(order.getCancelledBy())
                .refunded(wasRefunded)
                .refundedAmount(wasRefunded ? order.getTotalPrice() : null)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CANCELLED_KEY,
                event
        );



        log.info("Событие отмены заказа отправлено");
    }

    /**
     * Маппинг Order в OrderResponse
     */
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .email(order.getEmail())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .statusDisplayName(order.getStatus().getDisplayName())
                .itemCount(order.getItems().size())
                .items(order.getItems().stream()
                        .map(this::mapItemToResponse)
                        .collect(Collectors.toList()))
                .cancellationReason(order.getCancellationReason())
                .cancelledBy(order.getCancelledBy())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .paidAt(order.getPaidAt())
                .cancelledAt(order.getCancelledAt())
                .build();
    }

    /**
     * Маппинг OrderItem в OrderItemResponse
     */
    private OrderItemResponse mapItemToResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productPrice(item.getProductPrice())
                .productCategory(item.getProductCategory())
                .productImageUrl(item.getProductImageUrl())
                .quantity(item.getQuantity())
                .downloadLink(item.getDownloadLink())
                .addedAt(item.getAddedAt())
                .build();
    }
}