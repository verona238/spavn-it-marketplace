package com.spavnit.auth.repository;

import com.spavnit.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository для работы с пользователями в базе данных
 * Spring Data JPA автоматически создаст реализацию этих методов
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по email
     */
    Optional<User> findByEmail(String email);

    /**
     * Проверить существование пользователя по email
     */
    boolean existsByEmail(String email);
}