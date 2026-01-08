package com.spavnit.order.exception;

/**
 * Исключение когда корзина пуста
 */
public class EmptyCartException extends RuntimeException {

    public EmptyCartException(String message) {
        super(message);
    }
}