package com.spavnit.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Событие изменения роли из User Service
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