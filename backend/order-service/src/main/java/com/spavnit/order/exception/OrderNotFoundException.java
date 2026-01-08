package com.spavnit.order.exception;

/**
 * Исключение когда заказ не найден
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}