package com.spavnit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для изменения роли пользователя (только для администраторов)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequest {

    @NotBlank(message = "Роль обязательна")
    @Pattern(regexp = "CLIENT|ADMIN", message = "Роль должна быть CLIENT или ADMIN")
    private String role;
}