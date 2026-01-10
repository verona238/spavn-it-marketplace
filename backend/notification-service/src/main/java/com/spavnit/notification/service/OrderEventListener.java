package com.spavnit.notification.service;

import com.spavnit.notification.config.RabbitMQConfig;
import com.spavnit.notification.dto.OrderPaidEvent;
import com.spavnit.notification.dto.OrderCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final JavaMailSender mailSender;

    /**
     * Обработка события оплаты заказа - отправка ссылок на товары
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_PAID_QUEUE)
    public void handleOrderPaid(OrderPaidEvent event) {
        log.info("Получено событие оплаты заказа #{} для {}", event.getOrderId(), event.getEmail());

        try {
            // Формируем письмо со ссылками на товары
            StringBuilder body = new StringBuilder();
            body.append("Здравствуйте!\n\n");
            body.append("Ваш заказ #").append(event.getOrderId()).append(" успешно оплачен!\n\n");
            body.append("Сумма заказа: ").append(event.getTotalPrice()).append(" ₽\n\n");
            body.append("Ссылки для скачивания ваших товаров:\n\n");

            // Добавляем ссылки на каждый товар
            for (int i = 0; i < event.getProductLinks().size(); i++) {
                var link = event.getProductLinks().get(i);
                body.append(i + 1).append(". ").append(link.getProductName()).append("\n");
                body.append("   Ссылка: ").append(link.getDownloadLink()).append("\n\n");
            }

            body.append("Спасибо за покупку!\n\n");
            body.append("С уважением,\n");
            body.append("Команда SpavnIT");

            // Отправляем email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getEmail());
            message.setSubject("Ваш заказ #" + event.getOrderId() + " оплачен - ссылки на товары");
            message.setText(body.toString());

            mailSender.send(message);

            log.info("Email со ссылками на товары отправлен на: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Ошибка при отправке email со ссылками: {}", e.getMessage(), e);
        }
    }

    /**
     * Обработка события отмены заказа
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_CANCELLED_QUEUE)
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("Получено событие отмены заказа #{} для {}", event.getOrderId(), event.getEmail());

        try {
            StringBuilder body = new StringBuilder();
            body.append("Здравствуйте!\n\n");
            body.append("Ваш заказ #").append(event.getOrderId()).append(" был отменён.\n\n");
            body.append("Причина отмены: ").append(event.getCancellationReason()).append("\n\n");

            if (event.isRefunded()) {
                body.append("Средства в размере ").append(event.getRefundedAmount())
                        .append(" ₽ возвращены на ваш баланс.\n\n");
            }

            body.append("Приносим извинения за неудобства.\n\n");
            body.append("С уважением,\n");
            body.append("Команда SpavnIT");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getEmail());
            message.setSubject("Заказ #" + event.getOrderId() + " отменён");
            message.setText(body.toString());

            mailSender.send(message);

            log.info("Email об отмене заказа отправлен на: {}", event.getEmail());

        } catch (Exception e) {
            log.error("Ошибка при отправке email об отмене: {}", e.getMessage(), e);
        }
    }
}