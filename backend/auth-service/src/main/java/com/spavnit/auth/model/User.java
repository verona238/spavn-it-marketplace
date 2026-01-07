package com.spavnit.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Модель пользователя в системе
 * Эта таблица будет создана в базе данных auth_db
 */
@Entity
@Table(name = "users")
@Data  // Lombok создаст геттеры, сеттеры, toString, equals, hashCode
@NoArgsConstructor  // Создаст конструктор без параметров
@AllArgsConstructor  // Создаст конструктор со всеми параметрами
@Builder  // Позволит создавать объекты через паттерн Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;  // Будет храниться в виде bcrypt хэша

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @Column(name = "is_active")
    private boolean isActive = true;

    @Builder.Default
    @Column(name = "is_email_verified")
    private boolean isEmailVerified = false;

    // Для 2FA
    @Column(name = "two_factor_code")
    private String twoFactorCode;

    @Column(name = "two_factor_expiry")
    private LocalDateTime twoFactorExpiry;

    @Builder.Default
    @Column(name = "two_factor_enabled")
    private boolean twoFactorEnabled = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}