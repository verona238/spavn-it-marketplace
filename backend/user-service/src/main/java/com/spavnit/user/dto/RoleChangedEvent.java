package com.spavnit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Событие изменения роли пользователя (для отправки в Auth Service)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangedEvent {
    private Long userId;
    private String email;
    private String newRole;
}