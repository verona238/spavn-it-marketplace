package com.spavnit.user.exception;

/**
 * Исключение когда пользователь не найден
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}