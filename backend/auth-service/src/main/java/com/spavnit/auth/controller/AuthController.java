package com.spavnit.auth.controller;

import com.spavnit.auth.dto.*;
import com.spavnit.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "API для аутентификации и авторизации")
public class AuthController {

    private final AuthService authService;

    /**
     * Регистрация нового пользователя
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация", description = "Регистрация нового пользователя")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /register - Регистрация пользователя: {}", request.getEmail());
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Вход в систему
     */
    @PostMapping("/login")
    @Operation(summary = "Вход", description = "Вход в систему с опциональной 2FA для администраторов")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /login - Вход пользователя: {}", request.getEmail());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Подтверждение 2FA кода
     */
    @PostMapping("/verify-2fa")
    @Operation(summary = "Подтверждение 2FA", description = "Подтверждение двухфакторной аутентификации")
    public ResponseEntity<LoginResponse> verify2FA(@Valid @RequestBody TwoFactorRequest request) {
        log.info("POST /verify-2fa - Проверка 2FA для: {}", request.getEmail());
        LoginResponse response = authService.verify2FA(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running!");
    }
}