package com.spavnit.cart.exception;

/**
 * Исключение когда товар недоступен
 */
public class ProductNotAvailableException extends RuntimeException {

    public ProductNotAvailableException(String message) {
        super(message);
    }
}