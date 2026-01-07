package com.spavnit.user.service;

import com.spavnit.user.config.RabbitMQConfig;
import com.spavnit.user.dto.*;
import com.spavnit.user.exception.UserNotFoundException;
import com.spavnit.user.model.Role;
import com.spavnit.user.model.UserProfile;
import com.spavnit.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления профилями пользователей
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserEventPublisher userEventPublisher;

    /**
     * Слушаем событие создания пользователя из Auth Service
     */
    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    @Transactional
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Получено событие создания пользователя: {}", event.getEmail());

        if (userProfileRepository.existsByEmail(event.getEmail())) {
            log.warn("Профиль пользователя {} уже существует", event.getEmail());
            return;
        }

        UserProfile profile = UserProfile.builder()
                .id(event.getUserId())
                .email(event.getEmail())
                .role(Role.valueOf(event.getRole()))
                .isActive(true)
                .isEmailVerified(false)
                .build();

        userProfileRepository.save(profile);
        log.info("Профиль пользователя создан: {}", event.getEmail());
    }

    /**
     * Слушаем событие обновления пользователя из Auth Service
     */
    @RabbitListener(queues = RabbitMQConfig.USER_UPDATED_QUEUE)
    @Transactional
    public void handleUserUpdatedEvent(UserUpdatedEvent event) {
        log.info("Получено событие обновления пользователя: {}", event.getEmail());

        UserProfile profile = userProfileRepository.findById(event.getUserId())
                .orElse(null);

        if (profile == null) {
            // Если профиль не существует, создаём его
            log.warn("Профиль не найден, создаём новый для: {}", event.getEmail());
            profile = UserProfile.builder()
                    .id(event.getUserId())
                    .email(event.getEmail())
                    .role(Role.valueOf(event.getRole()))
                    .isActive(event.isActive())
                    .isEmailVerified(event.isEmailVerified())
                    .build();
        } else {
            // Обновляем существующий профиль
            profile.setRole(Role.valueOf(event.getRole()));
            profile.setActive(event.isActive());
            profile.setEmailVerified(event.isEmailVerified());
        }

        userProfileRepository.save(profile);
        log.info("Профиль пользователя синхронизирован: {}", event.getEmail());
    }

    /**
     * Слушаем событие удаления пользователя из Auth Service
     */
    @RabbitListener(queues = RabbitMQConfig.USER_DELETED_QUEUE)
    @Transactional
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        log.info("Получено событие удаления пользователя: {}", event.getEmail());

        userProfileRepository.findById(event.getUserId())
                .ifPresent(profile -> {
                    profile.setActive(false);
                    userProfileRepository.save(profile);
                    log.info("Пользователь деактивирован: {}", event.getEmail());
                });
    }



    /**
     * Получение профиля текущего пользователя
     */
    public UserProfileResponse getCurrentUserProfile(String email) {
        log.info("Получение профиля пользователя: {}", email);

        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Профиль пользователя не найден"));

        return mapToResponse(profile);
    }

    /**
     * Получение профиля пользователя по ID (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse getUserProfileById(Long id) {
        log.info("Администратор запросил профиль пользователя с ID: {}", id);

        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        return mapToResponse(profile);
    }

    /**
     * Получение всех пользователей (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllUsers() {
        log.info("Администратор запросил список всех пользователей");

        return userProfileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Обновление профиля текущего пользователя
     */
    @Transactional
    public UserProfileResponse updateCurrentUserProfile(String email, UpdateUserRequest request) {
        log.info("Обновление профиля пользователя: {}", email);

        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Профиль пользователя не найден"));

        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }

        userProfileRepository.save(profile);

        // ДОБАВЬТЕ: Отправляем событие обновления (для синхронизации с Auth)
        userEventPublisher.publishUserUpdated(profile);

        log.info("Профиль пользователя обновлен: {}", email);

        return mapToResponse(profile);
    }

    /**
     * Обновление профиля пользователя по ID (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserProfileResponse updateUserProfile(Long id, UpdateUserRequest request) {
        log.info("Администратор обновляет профиль пользователя с ID: {}", id);

        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }

        userProfileRepository.save(profile);

        // ДОБАВЬТЕ: Отправляем событие обновления
        userEventPublisher.publishUserUpdated(profile);

        log.info("Администратор обновил профиль пользователя с ID: {}", id);

        return mapToResponse(profile);
    }

    /**
     * Изменение роли пользователя (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserProfileResponse changeUserRole(Long id, ChangeRoleRequest request) {
        log.info("Администратор изменяет роль пользователя с ID: {} на {}", id, request.getRole());

        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        Role newRole = Role.valueOf(request.getRole());
        profile.setRole(newRole);
        userProfileRepository.save(profile);

        // Отправляем полное событие обновления (включает роль и все остальное)
        userEventPublisher.publishUserUpdated(profile);

        log.info("Роль пользователя {} изменена на {}", profile.getEmail(), newRole);

        return mapToResponse(profile);
    }

    /**
     * Удаление пользователя (деактивация) (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(Long id) {
        log.info("Администратор деактивирует пользователя с ID: {}", id);

        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        profile.setActive(false);
        userProfileRepository.save(profile);

        // Отправляем событие обновления с is_active = false
        userEventPublisher.publishUserDeactivated(profile);

        log.info("Пользователь {} деактивирован", profile.getEmail());
    }

    /**
     * Маппинг UserProfile в UserProfileResponse
     */
    private UserProfileResponse mapToResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .role(profile.getRole().name())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .isActive(profile.isActive())
                .isEmailVerified(profile.isEmailVerified())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}