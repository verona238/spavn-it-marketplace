package com.spavnit.order.controller;

import com.spavnit.order.dto.CancelOrderRequest;
import com.spavnit.order.dto.CreateOrderRequest;
import com.spavnit.order.dto.OrderResponse;
import com.spavnit.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления заказами
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Management", description = "API для управления заказами")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    private final OrderService orderService;

    /**
     * Создание заказа из корзины
     * POST /api/orders
     */
    @PostMapping
    @Operation(summary = "Создать заказ",
            description = "Создание заказа из текущей корзины пользователя")
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication,
            HttpServletRequest request,
            @RequestBody CreateOrderRequest createRequest) {

        log.info("POST / - Создание заказа для пользователя: {}", authentication.getName());

        String token = extractToken(request);
        Long userId = (Long) request.getAttribute("userId");

        OrderResponse order = orderService.createOrder(authentication.getName(), userId, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    /**
     * Оплата заказа
     * POST /api/orders/{orderId}/pay
     */
    @PostMapping("/{orderId}/pay")
    @Operation(summary = "Оплатить заказ",
            description = "Оплата заказа списанием средств с баланса")
    public ResponseEntity<OrderResponse> payOrder(
            Authentication authentication,
            HttpServletRequest request,
            @PathVariable Long orderId) {

        log.info("POST /{}/pay - Оплата заказа пользователем: {}", orderId, authentication.getName());

        String token = extractToken(request);

        OrderResponse order = orderService.payOrder(authentication.getName(), orderId, token);

        return ResponseEntity.ok(order);
    }

    /**
     * Отмена заказа (только для администраторов)
     * POST /api/orders/{orderId}/cancel
     */
    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Отменить заказ (ADMIN)",
            description = "Отмена заказа администратором. " +
                    "Если заказ был оплачен, средства возвращаются на баланс пользователя. " +
                    "Причина отмены обязательна.")
    public ResponseEntity<OrderResponse> cancelOrder(
            Authentication authentication,
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderRequest request) {

        log.info("POST /{}/cancel - Администратор {} отменяет заказ", orderId, authentication.getName());

        OrderResponse order = orderService.cancelOrder(authentication.getName(), orderId, request);

        return ResponseEntity.ok(order);
    }



    /**
     * Получение заказа по ID
     * GET /api/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Получить заказ по ID",
            description = "Получение детальной информации о заказе")
    public ResponseEntity<OrderResponse> getOrderById(
            Authentication authentication,
            @PathVariable Long orderId) {

        log.info("GET /{} - Запрос заказа пользователем: {}", orderId, authentication.getName());

        OrderResponse order = orderService.getOrderById(orderId, authentication.getName());
        return ResponseEntity.ok(order);
    }

    /**
     * Получение всех заказов текущего пользователя
     * GET /api/orders/my
     */
    @GetMapping("/my")
    @Operation(summary = "Получить свои заказы",
            description = "Получение списка всех заказов текущего пользователя")
    public ResponseEntity<List<OrderResponse>> getUserOrders(Authentication authentication) {
        log.info("GET /my - Запрос всех заказов пользователя: {}", authentication.getName());

        List<OrderResponse> orders = orderService.getUserOrders(authentication.getName());
        return ResponseEntity.ok(orders);
    }

    /**
     * Получение всех заказов (только для администраторов)
     * GET /api/orders/admin/all
     */
    @GetMapping("/admin/all")
    @Operation(summary = "Получить все заказы (ADMIN)",
            description = "Получение списка всех заказов всех пользователей. Доступно только администраторам.")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("GET /admin/all - Администратор запросил все заказы");

        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Health check эндпоинт
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Service is running! 🛒");
    }

    /**
     * Извлечение JWT токена из заголовка
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}