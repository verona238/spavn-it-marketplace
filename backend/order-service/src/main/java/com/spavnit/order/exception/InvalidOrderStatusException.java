package com.spavnit.order.exception;

/**
 * Исключение при недопустимом статусе заказа для операции
 */
public class InvalidOrderStatusException extends RuntimeException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }
}