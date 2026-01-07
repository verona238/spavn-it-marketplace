package com.spavnit.auth.dto;

/**
 * Типы email уведомлений
 */
public enum EmailType {
    TWO_FACTOR_CODE,      // Код 2FA
    WELCOME,              // Приветственное письмо
    PASSWORD_RESET,       // Сброс пароля
    ORDER_CONFIRMATION    // Подтверждение заказа
}