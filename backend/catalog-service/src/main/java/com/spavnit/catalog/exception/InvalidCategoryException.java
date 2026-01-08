package com.spavnit.catalog.exception;

/**
 * Исключение при некорректной категории
 */
public class InvalidCategoryException extends RuntimeException {

    public InvalidCategoryException(String message) {
        super(message);
    }
}