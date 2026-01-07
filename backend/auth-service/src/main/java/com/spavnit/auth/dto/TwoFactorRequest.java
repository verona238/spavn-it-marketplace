package com.spavnit.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для подтверждения 2FA кода
 */
@Data
public class TwoFactorRequest {

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Код обязателен")
    @Size(min = 6, max = 6, message = "Код должен содержать 6 цифр")
    private String code;
}