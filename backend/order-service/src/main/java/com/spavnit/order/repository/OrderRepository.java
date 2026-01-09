package com.spavnit.order.repository;

import com.spavnit.order.model.Order;
import com.spavnit.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByEmailOrderByCreatedAtDesc(String email);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findAllByOrderByCreatedAtDesc();
}