package com.spavnit.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Главный класс приложения Auth Service
 * Это точка входа в приложение
 */
@SpringBootApplication
@EnableScheduling  // Включаем возможность использовать планировщик задач
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
        System.out.println("Auth Service запущен!");
        System.out.println("Swagger UI: http://localhost:8081/api/auth/swagger-ui.html");
    }
}