package com.spavnit.order.repository;

import com.spavnit.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository для работы с товарами в заказе
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}