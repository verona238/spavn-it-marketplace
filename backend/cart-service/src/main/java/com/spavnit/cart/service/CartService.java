package com.spavnit.cart.service;

import com.spavnit.cart.client.CatalogClient;
import com.spavnit.cart.dto.*;
import com.spavnit.cart.exception.CartItemNotFoundException;
import com.spavnit.cart.exception.CartNotFoundException;
import com.spavnit.cart.exception.ProductNotAvailableException;
import com.spavnit.cart.model.Cart;
import com.spavnit.cart.model.CartItem;
import com.spavnit.cart.repository.CartItemRepository;
import com.spavnit.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления корзиной покупок
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CatalogClient catalogClient;

    /**
     * Получение корзины текущего пользователя
     */
    public CartResponse getCurrentUserCart(String email) {
        log.info("Получение корзины пользователя: {}", email);

        Cart cart = cartRepository.findByEmail(email)
                .orElseGet(() -> createEmptyCart(email));

        return mapToResponse(cart);
    }

    /**
     * Добавление товара в корзину
     * Требование: возможность добавления товаров в корзину
     */
    /**
     * Добавление товара в корзину
     * Требование: возможность добавления товаров в корзину
     * Ограничение: каждый товар только в 1 экземпляре (цифровые товары)
     */
    @Transactional
    public CartResponse addToCart(String email, AddToCartRequest request) {
        log.info("Добавление товара {} в корзину пользователя: {}", request.getProductId(), email);

        // Получаем информацию о товаре из Catalog Service
        ProductInfo product = catalogClient.getProductInfo(request.getProductId());

        if (product == null) {
            throw new ProductNotAvailableException("Товар с ID " + request.getProductId() + " не найден");
        }

        if (!product.isAvailable()) {
            throw new ProductNotAvailableException("Товар \"" + product.getName() + "\" недоступен для покупки");
        }

        // Получаем или создаём корзину
        Cart cart = cartRepository.findByEmail(email)
                .orElseGet(() -> createEmptyCart(email));

        // Проверяем, есть ли уже такой товар в корзине
        boolean itemExists = cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(request.getProductId()));

        if (itemExists) {
            throw new IllegalArgumentException("Товар \"" + product.getName() + "\" уже добавлен в корзину. " +
                    "Каждый цифровой товар можно добавить только один раз.");
        }

        // Создаём новую позицию в корзине с quantity = 1
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .productCategory(product.getCategoryDisplayName())
                .productImageUrl(product.getImageUrl())
                .quantity(1) // Всегда 1
                .build();

        cart.addItem(newItem);
        log.info("Товар {} добавлен в корзину (1 экземпляр)", product.getName());

        cart = cartRepository.save(cart);
        log.info("Корзина обновлена");

        return mapToResponse(cart);
    }

    /**
     * Удаление товара из корзины
     * Требование: возможность удаления товаров из корзины
     */
    @Transactional
    public CartResponse removeFromCart(String email, Long itemId) {
        log.info("Удаление товара {} из корзины пользователя: {}", itemId, email);

        Cart cart = cartRepository.findByEmail(email)
                .orElseThrow(() -> new CartNotFoundException("Корзина не найдена"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Товар с ID " + itemId + " не найден в корзине"));

        cart.removeItem(item);
        cartItemRepository.delete(item);
        cart = cartRepository.save(cart);

        log.info("Товар удален из корзины");

        return mapToResponse(cart);
    }

    /**
     * Очистка корзины
     */
    @Transactional
    public void clearCart(String email) {
        log.info("Очистка корзины пользователя: {}", email);

        Cart cart = cartRepository.findByEmail(email)
                .orElseThrow(() -> new CartNotFoundException("Корзина не найдена"));

        cart.clear();
        cartRepository.save(cart);

        log.info("Корзина очищена");
    }

    /**
     * Создание пустой корзины для нового пользователя
     */
    private Cart createEmptyCart(String email) {
        log.info("Создание новой корзины для пользователя: {}", email);

        // Здесь в реальном приложении нужно было бы получить userId из User Service
        // Но для упрощения используем email
        Cart cart = Cart.builder()
                .email(email)
                .userId(0L) // Временное значение
                .build();

        return cartRepository.save(cart);
    }

    /**
     * Маппинг Cart в CartResponse
     */
    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());

        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .email(cart.getEmail())
                .items(itemResponses)
                .totalItems(totalItems)
                .totalPrice(totalPrice)
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    /**
     * Маппинг CartItem в CartItemResponse
     */
    private CartItemResponse mapToItemResponse(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productPrice(item.getProductPrice())
                .productCategory(item.getProductCategory())
                .productImageUrl(item.getProductImageUrl())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .addedAt(item.getAddedAt())
                .build();
    }
}