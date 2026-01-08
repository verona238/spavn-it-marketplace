package com.spavnit.balance.service;

import com.spavnit.balance.config.RabbitMQConfig;
import com.spavnit.balance.dto.*;
import com.spavnit.balance.exception.BalanceNotFoundException;
import com.spavnit.balance.exception.InsufficientFundsException;
import com.spavnit.balance.model.Balance;
import com.spavnit.balance.model.Transaction;
import com.spavnit.balance.model.TransactionType;
import com.spavnit.balance.repository.BalanceRepository;
import com.spavnit.balance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления балансами пользователей
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${balance.initial-amount}")
    private BigDecimal initialAmount;

    /**
     * Слушаем событие создания пользователя из Auth Service
     * Создаем баланс с начальной суммой 100 монет
     */
    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    @Transactional
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Получено событие создания пользователя: {}", event.getEmail());

        if (balanceRepository.existsByUserId(event.getUserId())) {
            log.warn("Баланс для пользователя {} уже существует", event.getEmail());
            return;
        }

        // Создаем баланс с начальной суммой
        Balance balance = Balance.builder()
                .userId(event.getUserId())
                .email(event.getEmail())
                .amount(initialAmount)
                .build();

        balanceRepository.save(balance);

        // Создаем транзакцию начисления начального баланса
        Transaction transaction = Transaction.builder()
                .userId(event.getUserId())
                .email(event.getEmail())
                .type(TransactionType.INITIAL_BALANCE)
                .amount(initialAmount)
                .balanceBefore(BigDecimal.ZERO)
                .balanceAfter(initialAmount)
                .description("Начальный баланс при регистрации")
                .build();

        transactionRepository.save(transaction);

        log.info("Баланс создан для пользователя {}: {} монет", event.getEmail(), initialAmount);
    }

    /**
     * Получение баланса текущего пользователя
     */
    public BalanceResponse getCurrentUserBalance(String email) {
        log.info("Получение баланса пользователя: {}", email);

        Balance balance = balanceRepository.findByEmail(email)
                .orElseThrow(() -> new BalanceNotFoundException("Баланс не найден"));

        return mapToResponse(balance);
    }

    /**
     * Получение баланса пользователя по ID (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public BalanceResponse getBalanceByUserId(Long userId) {
        log.info("Администратор запросил баланс пользователя с ID: {}", userId);

        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new BalanceNotFoundException("Баланс пользователя с ID " + userId + " не найден"));

        return mapToResponse(balance);
    }



    /**
     * Получение всех балансов (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<BalanceResponse> getAllBalances() {
        log.info("Администратор запросил список всех балансов");

        return balanceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Списание средств с баланса (для оплаты заказа)
     * Используется Payment Service
     */
    @Transactional
    public BalanceResponse debitBalance(String email, DebitRequest request) {
        log.info("Списание {} монет с баланса пользователя: {}", request.getAmount(), email);

        Balance balance = balanceRepository.findByEmail(email)
                .orElseThrow(() -> new BalanceNotFoundException("Баланс не найден"));

        // Проверяем достаточность средств
        if (balance.getAmount().compareTo(request.getAmount()) < 0) {
            log.warn("Недостаточно средств для списания. Баланс: {}, Требуется: {}",
                    balance.getAmount(), request.getAmount());
            throw new InsufficientFundsException(
                    String.format("Недостаточно средств для оплаты товара. Ваш баланс: %.2f монет, требуется: %.2f монет",
                            balance.getAmount(), request.getAmount())
            );
        }

        BigDecimal balanceBefore = balance.getAmount();
        BigDecimal balanceAfter = balanceBefore.subtract(request.getAmount());

        balance.setAmount(balanceAfter);
        balanceRepository.save(balance);

        // Создаем транзакцию списания
        Transaction transaction = Transaction.builder()
                .userId(balance.getUserId())
                .email(balance.getEmail())
                .type(TransactionType.DEBIT)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(request.getDescription() != null ? request.getDescription() : "Оплата заказа")
                .orderId(request.getOrderId())
                .build();

        transactionRepository.save(transaction);

        log.info("Списание выполнено. Новый баланс: {} монет", balanceAfter);

        // Отправляем событие успешной оплаты
        publishPaymentEvent(balance, request, "SUCCESS", "Оплата успешно выполнена");

        return mapToResponse(balance);
    }

    /**
     * Возврат средств (при отмене заказа администратором)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BalanceResponse refundBalance(Long userId, DebitRequest request) {
        log.info("Администратор возвращает {} монет пользователю с ID: {}", request.getAmount(), userId);

        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new BalanceNotFoundException("Баланс пользователя с ID " + userId + " не найден"));

        BigDecimal balanceBefore = balance.getAmount();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());

        balance.setAmount(balanceAfter);
        balanceRepository.save(balance);

        // Создаем транзакцию возврата
        Transaction transaction = Transaction.builder()
                .userId(balance.getUserId())
                .email(balance.getEmail())
                .type(TransactionType.REFUND)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(request.getDescription() != null ? request.getDescription() : "Возврат средств за отмененный заказ")
                .orderId(request.getOrderId())
                .build();

        transactionRepository.save(transaction);

        log.info("Возврат выполнен. Новый баланс: {} монет", balanceAfter);

        return mapToResponse(balance);
    }



    /**
     * Корректировка баланса администратором
     * Может добавлять или вычитать средства
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BalanceResponse adjustBalance(Long userId, AdminAdjustmentRequest request) {
        log.info("Администратор корректирует баланс пользователя с ID: {} на {} монет", userId, request.getAmount());

        Balance balance = balanceRepository.findById(userId)
                .orElseThrow(() -> new BalanceNotFoundException("Баланс пользователя с ID " + userId + " не найден"));

        BigDecimal balanceBefore = balance.getAmount();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());

        // Проверяем, что баланс не станет отрицательным
        if (balanceAfter.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Корректировка приведет к отрицательному балансу");
        }

        balance.setAmount(balanceAfter);
        balanceRepository.save(balance);

        // Создаем транзакцию корректировки
        Transaction transaction = Transaction.builder()
                .userId(balance.getUserId())
                .email(balance.getEmail())
                .type(TransactionType.ADMIN_ADJUSTMENT)
                .amount(request.getAmount().abs())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(request.getDescription())
                .build();

        transactionRepository.save(transaction);

        log.info("Корректировка выполнена. Новый баланс: {} монет", balanceAfter);

        return mapToResponse(balance);
    }

    /**
     * Получение истории транзакций текущего пользователя
     */
    public List<TransactionResponse> getCurrentUserTransactions(String email) {
        log.info("Получение истории транзакций пользователя: {}", email);

        List<Transaction> transactions = transactionRepository.findByEmailOrderByCreatedAtDesc(email);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение истории транзакций пользователя по ID (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionResponse> getTransactionsByUserId(Long userId) {
        log.info("Администратор запросил историю транзакций пользователя с ID: {}", userId);

        List<Transaction> transactions = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получение всех транзакций (только для администраторов)
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionResponse> getAllTransactions() {
        log.info("Администратор запросил все транзакции");

        return transactionRepository.findAll().stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Публикация события результата оплаты
     */
    private void publishPaymentEvent(Balance balance, DebitRequest request, String status, String message) {
        PaymentEvent event = PaymentEvent.builder()
                .orderId(request.getOrderId())
                .userId(balance.getUserId())
                .email(balance.getEmail())
                .amount(request.getAmount())
                .status(status)
                .message(message)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EVENTS_EXCHANGE,
                RabbitMQConfig.PAYMENT_RESULT_KEY,
                event
        );

        log.info("Событие оплаты отправлено: статус={}, заказ={}", status, request.getOrderId());
    }



    /**
     * Маппинг Balance в BalanceResponse
     */
    private BalanceResponse mapToResponse(Balance balance) {
        return BalanceResponse.builder()
                .userId(balance.getUserId())
                .email(balance.getEmail())
                .amount(balance.getAmount())
                .createdAt(balance.getCreatedAt())
                .updatedAt(balance.getUpdatedAt())
                .build();
    }

    /**
     * Маппинг Transaction в TransactionResponse
     */
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .email(transaction.getEmail())
                .type(transaction.getType().name())
                .amount(transaction.getAmount())
                .balanceBefore(transaction.getBalanceBefore())
                .balanceAfter(transaction.getBalanceAfter())
                .description(transaction.getDescription())
                .orderId(transaction.getOrderId())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}