package com.spavnit.balance.controller;

import com.spavnit.balance.dto.*;
import com.spavnit.balance.service.BalanceService;
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
 * REST контроллер для управления балансами
 * Все эндпоинты доступны по адресу: http://localhost:8084/api/balance
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Balance Management", description = "API для управления виртуальным балансом пользователей")
@SecurityRequirement(name = "Bearer Authentication")
public class BalanceController {

    private final BalanceService balanceService;

    /**
     * Получение баланса текущего пользователя
     * GET /api/balance/me
     */
    @GetMapping("/me")
    @Operation(summary = "Получить свой баланс",
            description = "Получение баланса текущего пользователя")
    public ResponseEntity<BalanceResponse> getCurrentUserBalance(Authentication authentication) {
        log.info("GET /me - Запрос баланса пользователя: {}", authentication.getName());
        BalanceResponse response = balanceService.getCurrentUserBalance(authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Получение истории транзакций текущего пользователя
     * GET /api/balance/me/transactions
     */
    @GetMapping("/me/transactions")
    @Operation(summary = "Получить историю своих транзакций",
            description = "Получение истории всех транзакций текущего пользователя")
    public ResponseEntity<List<TransactionResponse>> getCurrentUserTransactions(Authentication authentication) {
        log.info("GET /me/transactions - Запрос истории транзакций пользователя: {}", authentication.getName());
        List<TransactionResponse> transactions = balanceService.getCurrentUserTransactions(authentication.getName());
        return ResponseEntity.ok(transactions);
    }

    /**
     * Списание средств с баланса
     * POST /api/balance/debit
     * Используется Payment Service для оплаты заказов
     */
    @PostMapping("/debit")
    @Operation(summary = "Списать средства с баланса",
            description = "Списание средств с баланса пользователя (используется для оплаты заказов)")
    public ResponseEntity<BalanceResponse> debitBalance(
            Authentication authentication,
            @Valid @RequestBody DebitRequest request) {
        log.info("POST /debit - Списание средств с баланса пользователя: {}", authentication.getName());
        BalanceResponse response = balanceService.debitBalance(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * Получение всех балансов (только для администраторов)
     * GET /api/balance
     */
    @GetMapping
    @Operation(summary = "Получить все балансы (ADMIN)",
            description = "Получение списка всех балансов. Доступно только администраторам")
    public ResponseEntity<List<BalanceResponse>> getAllBalances() {
        log.info("GET / - Запрос списка всех балансов");
        List<BalanceResponse> balances = balanceService.getAllBalances();
        return ResponseEntity.ok(balances);
    }



    /**
     * Получение баланса пользователя по ID (только для администраторов)
     * GET /api/balance/{userId}
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Получить баланс по ID пользователя (ADMIN)",
            description = "Получение баланса пользователя по ID. Доступно только администраторам")
    public ResponseEntity<BalanceResponse> getBalanceByUserId(@PathVariable Long userId) {
        log.info("GET /{} - Запрос баланса пользователя с ID: {}", userId, userId);
        BalanceResponse response = balanceService.getBalanceByUserId(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Получение транзакций пользователя по ID (только для администраторов)
     * GET /api/balance/{userId}/transactions
     */
    @GetMapping("/{userId}/transactions")
    @Operation(summary = "Получить транзакции по ID пользователя (ADMIN)",
            description = "Получение истории транзакций пользователя по ID. Доступно только администраторам")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUserId(@PathVariable Long userId) {
        log.info("GET /{}/transactions - Запрос транзакций пользователя с ID: {}", userId, userId);
        List<TransactionResponse> transactions = balanceService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Получение всех транзакций (только для администраторов)
     * GET /api/balance/transactions/all
     */
    @GetMapping("/transactions/all")
    @Operation(summary = "Получить все транзакции (ADMIN)",
            description = "Получение списка всех транзакций. Доступно только администраторам")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        log.info("GET /transactions/all - Запрос всех транзакций");
        List<TransactionResponse> transactions = balanceService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Возврат средств (только для администраторов)
     * POST /api/balance/{userId}/refund
     */
    @PostMapping("/{userId}/refund")
    @Operation(summary = "Вернуть средства (ADMIN)",
            description = "Возврат средств пользователю (при отмене заказа). Доступно только администраторам")
    public ResponseEntity<BalanceResponse> refundBalance(
            @PathVariable Long userId,
            @Valid @RequestBody DebitRequest request) {
        log.info("POST /{}/refund - Возврат средств пользователю с ID: {}", userId, userId);
        BalanceResponse response = balanceService.refundBalance(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Корректировка баланса (только для администраторов)
     * PATCH /api/balance/{userId}/adjust
     */
    @PatchMapping("/{userId}/adjust")
    @Operation(summary = "Корректировать баланс (ADMIN)",
            description = "Корректировка баланса пользователя (добавление/вычитание средств). Доступно только администраторам")
    public ResponseEntity<BalanceResponse> adjustBalance(
            @PathVariable Long userId,
            @Valid @RequestBody AdminAdjustmentRequest request) {
        log.info("PATCH /{}/adjust - Корректировка баланса пользователя с ID: {}", userId, userId);
        BalanceResponse response = balanceService.adjustBalance(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check эндпоинт
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Проверка работоспособности сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Balance Service is running!");
    }
}