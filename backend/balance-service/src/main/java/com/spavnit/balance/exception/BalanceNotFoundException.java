package com.spavnit.balance.exception;

/**
 * Исключение когда баланс пользователя не найден
 */
public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException(String message) {
        super(message);
    }
}