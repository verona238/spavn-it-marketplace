package com.spavnit.balance.model;

/**
 * Типы транзакций
 */
public enum TransactionType {
    INITIAL_BALANCE,    // Начисление начального баланса при регистрации (100 монет)
    DEBIT,              // Списание (оплата заказа)
    REFUND,             // Возврат средств (при отмене заказа администратором)
    ADMIN_ADJUSTMENT    // Корректировка баланса администратором
}