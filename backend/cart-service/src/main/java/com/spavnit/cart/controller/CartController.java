package com.spavnit.cart.controller;

import com.spavnit.cart.dto.*;
import com.spavnit.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для управления корзиной покупок
 * Все эндпоинты доступны по адресу: http://localhost:8086/api/cart
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart Management", description = "API для управления корзиной покупок")
@SecurityRequirement(name = "Bearer Authentication")
public class CartController {

    private final CartService cartService;

    /**
     * Получение корзины текущего пользователя
     * GET /api/cart
     */
    @GetMapping
    @Operation(summary = "Получить свою корзину",
            description = "Получение содержимого корзины текущего пользователя")
    public ResponseEntity<CartResponse> getCurrentUserCart(Authentication authentication) {
        log.info("GET / - Запрос корзины пользователя: {}", authentication.getName());
        CartResponse cart = cartService.getCurrentUserCart(authentication.getName());
        return ResponseEntity.ok(cart);
    }

    /**
     * Добавление товара в корзину (только в одном экземпляре)
     * POST /api/cart/items
     */
    @PostMapping("/items")
    @Operation(summary = "Добавить товар в корзину",
            description = "Добавление товара в корзину. Если товар уже есть, увеличивается количество")
    public ResponseEntity<CartResponse> addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request) {
        log.info("POST /items - Добавление товара {} в корзину пользователя: {}",
                request.getProductId(), authentication.getName());
        CartResponse cart = cartService.addToCart(authentication.getName(), request);
        return ResponseEntity.ok(cart);
    }

    /**
     * Удаление товара из корзины
     * DELETE /api/cart/items/{itemId}
     */
    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Удалить товар из корзины",
            description = "Удаление товара из корзины")
    public ResponseEntity<CartResponse> removeFromCart(
            Authentication authentication,
            @PathVariable Long itemId) {
        log.info("DELETE /items/{} - Удаление товара из корзины пользователя: {}",
                itemId, authentication.getName());
        CartResponse cart = cartService.removeFromCart(authentication.getName(), itemId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Очистить корзину
     * DELETE /api/cart/clear
     */
    @DeleteMapping("/clear")
    @Operation(summary = "Очистить корзину",
            description = "Удаление всех товаров из корзины")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        log.info("DELETE /clear - Очистка корзины для пользователя: {}", authentication.getName());

        cartService.clearCart(authentication.getName());

        return ResponseEntity.noContent().build();
    }



    /**
     * Health check эндпоинт
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Cart Service is running! 🛒");
    }
}