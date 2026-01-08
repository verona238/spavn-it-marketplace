package com.spavnit.balance.repository;

import com.spavnit.balance.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository для работы с балансами
 */
@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Optional<Balance> findByEmail(String email);

    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);
}