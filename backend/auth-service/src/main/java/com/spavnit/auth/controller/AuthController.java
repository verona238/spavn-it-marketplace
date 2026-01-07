package com.spavnit.auth.controller;

import com.spavnit.auth.dto.*;
import com.spavnit.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для авторизации и аутентификации
 * Все эндпоинты доступны по адресу: http://localhost:8081/api/auth
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "API для авторизации и регистрации")
public class AuthController {

    private final AuthService authService;

    /**
     * Регистрация нового пользователя
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя",
            description = "Регистрация нового пользователя по email")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /register - Регистрация пользователя: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Вход в систему
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Вход в систему",
            description = "Авторизация пользователя. Для администраторов требуется 2FA")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /login - Вход пользователя: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Подтверждение 2FA кода
     * POST /api/auth/verify-2fa
     */
    @PostMapping("/verify-2fa")
    @Operation(summary = "Подтверждение 2FA",
            description = "Подтверждение кода двухфакторной аутентификации")
    public ResponseEntity<AuthResponse> verifyTwoFactor(@Valid @RequestBody TwoFactorRequest request) {
        log.info("POST /verify-2fa - Проверка 2FA для: {}", request.getEmail());
        AuthResponse response = authService.verifyTwoFactor(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check эндпоинт
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running!");
    }
}