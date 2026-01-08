package com.spavnit.order.exception;

/**
 * Исключение при неудачной оплате
 */
public class PaymentFailedException extends RuntimeException {

    public PaymentFailedException(String message) {
        super(message);
    }
}