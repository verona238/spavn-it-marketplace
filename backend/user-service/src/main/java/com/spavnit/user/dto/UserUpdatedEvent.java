package com.spavnit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Событие обновления пользователя из Auth Service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatedEvent {
    private Long userId;
    private String email;
    private String role;
    private boolean isActive;
    private boolean isEmailVerified;
}