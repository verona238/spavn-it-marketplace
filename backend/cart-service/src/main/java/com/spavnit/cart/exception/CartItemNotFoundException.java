package com.spavnit.cart.exception;

/**
 * Исключение когда товар в корзине не найден
 */
public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(String message) {
        super(message);
    }
}