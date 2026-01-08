package com.spavnit.order.model;

/**
 * Статусы заказа
 */
public enum OrderStatus {
    CREATED("Сформирован"),      // Заказ создан, ожидает оплаты
    PAID("Выполнен"),            // Заказ оплачен, товары отправлены
    CANCELLED("Отменен");        // Заказ отменен администратором

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}