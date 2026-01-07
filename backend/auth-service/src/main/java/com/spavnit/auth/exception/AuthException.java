package com.spavnit.auth.exception;

/**
 * Кастомное исключение для ошибок авторизации
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}