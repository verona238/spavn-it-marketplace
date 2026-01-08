package com.spavnit.balance.exception;

/**
 * Исключение при недостатке средств на балансе
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}