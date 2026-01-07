package com.spavnit.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Notification Service
 */
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
        System.out.println("Notification Service запущен!");
        System.out.println("Swagger UI: http://localhost:8082/api/notifications/swagger-ui.html");
    }
}