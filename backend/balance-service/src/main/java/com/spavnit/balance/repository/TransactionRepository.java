package com.spavnit.balance.repository;

import com.spavnit.balance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для работы с транзакциями
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Transaction> findByEmailOrderByCreatedAtDesc(String email);

    List<Transaction> findByOrderId(Long orderId);
}