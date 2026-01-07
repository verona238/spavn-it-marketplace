package com.spavnit.notification.service;

import com.spavnit.notification.dto.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки email
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String fromEmail;

    @Value("${email.from-name}")
    private String fromName;

    /**
     * Отправка email
     */
    public void sendEmail(EmailEvent emailEvent) {
        try {
            log.info("Отправка email на: {} с темой: {}", emailEvent.getTo(), emailEvent.getSubject());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromName + " <" + fromEmail + ">");
            message.setTo(emailEvent.getTo());
            message.setSubject(emailEvent.getSubject());
            message.setText(emailEvent.getBody());

            mailSender.send(message);

            log.info("Email успешно отправлен на: {}", emailEvent.getTo());
        } catch (Exception e) {
            log.error("Ошибка при отправке email на {}: {}", emailEvent.getTo(), e.getMessage());
            throw new RuntimeException("Не удалось отправить email", e);
        }
    }
}