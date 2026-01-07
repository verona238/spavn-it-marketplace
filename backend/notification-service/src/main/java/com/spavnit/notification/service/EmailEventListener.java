package com.spavnit.notification.service;

import com.spavnit.notification.config.RabbitMQConfig;
import com.spavnit.notification.dto.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Listener для обработки событий email из RabbitMQ
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailEventListener {

    private final EmailService emailService;

    /**
     * Слушаем очередь email и отправляем письма
     */
    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmailEvent(EmailEvent emailEvent) {
        log.info("📩 Получено событие email из RabbitMQ: type={}, to={}",
                emailEvent.getType(), emailEvent.getTo());

        try {
            emailService.sendEmail(emailEvent);
            log.info("Email событие обработано успешно");
        } catch (Exception e) {
            log.error("Ошибка при обработке email события: {}", e.getMessage());
            // В реальном проекте здесь можно добавить retry логику или DLQ (Dead Letter Queue)
        }
    }
}