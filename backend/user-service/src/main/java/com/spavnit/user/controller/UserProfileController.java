package com.spavnit.user.controller;

import com.spavnit.user.dto.ChangeRoleRequest;
import com.spavnit.user.dto.UpdateUserRequest;
import com.spavnit.user.dto.UserProfileResponse;
import com.spavnit.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления профилями пользователей
 * Все эндпоинты доступны по адресу: http://localhost:8083/api/users
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Profile Management", description = "API для управления профилями пользователей")
@SecurityRequirement(name = "Bearer Authentication")
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * Получение профиля текущего пользователя
     * GET /api/users/me
     */
    @GetMapping("/me")
    @Operation(summary = "Получить свой профиль",
            description = "Получение профиля текущего аутентифицированного пользователя")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(Authentication authentication) {
        log.info("GET /me - Запрос профиля пользователя: {}", authentication.getName());
        UserProfileResponse response = userProfileService.getCurrentUserProfile(authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Обновление профиля текущего пользователя
     * PUT /api/users/me
     */
    @PutMapping("/me")
    @Operation(summary = "Обновить свой профиль",
            description = "Обновление профиля текущего пользователя (имя, фамилия, телефон)")
    public ResponseEntity<UserProfileResponse> updateCurrentUserProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("PUT /me - Обновление профиля пользователя: {}", authentication.getName());
        UserProfileResponse response = userProfileService.updateCurrentUserProfile(
                authentication.getName(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * Получение всех пользователей (только для администраторов)
     * GET /api/users
     */
    @GetMapping
    @Operation(summary = "Получить всех пользователей (ADMIN)",
            description = "Получение списка всех пользователей. Доступно только администраторам")
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        log.info("GET / - Запрос списка всех пользователей");
        List<UserProfileResponse> users = userProfileService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Получение пользователя по ID (только для администраторов)
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID (ADMIN)",
            description = "Получение профиля пользователя по ID. Доступно только администраторам")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        log.info("GET /{} - Запрос профиля пользователя с ID: {}", id, id);
        UserProfileResponse response = userProfileService.getUserProfileById(id);
        return ResponseEntity.ok(response);
    }


    /**
     * Обновление пользователя по ID (только для администраторов)
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя по ID (ADMIN)",
            description = "Обновление профиля пользователя по ID. Доступно только администраторам")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("PUT /{} - Обновление профиля пользователя с ID: {}", id, id);
        UserProfileResponse response = userProfileService.updateUserProfile(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Изменение роли пользователя (только для администраторов)
     * PATCH /api/users/{id}/role
     */
    @PatchMapping("/{id}/role")
    @Operation(summary = "Изменить роль пользователя (ADMIN)",
            description = "Изменение роли пользователя (CLIENT/ADMIN). Доступно только администраторам")
    public ResponseEntity<UserProfileResponse> changeUserRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request) {
        log.info("PATCH /{}/role - Изменение роли пользователя с ID: {} на {}", id, id, request.getRole());
        UserProfileResponse response = userProfileService.changeUserRole(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Удаление (деактивация) пользователя (только для администраторов)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя (ADMIN)",
            description = "Деактивация пользователя. Доступно только администраторам")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /{} - Деактивация пользователя с ID: {}", id, id);
        userProfileService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check эндпоинт
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running!");
    }
}