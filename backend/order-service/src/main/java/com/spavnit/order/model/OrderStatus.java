package com.spavnit.order.model;

public enum OrderStatus {
    CREATED("Создан"),
    PAID("Оплачен"),
    CANCELLED("Отменён");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}