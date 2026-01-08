package com.spavnit.cart.exception;

/**
 * Исключение когда корзина не найдена
 */
public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(String message) {
        super(message);
    }
}