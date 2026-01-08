package com.spavnit.balance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Balance Service
 */
@SpringBootApplication
public class BalanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BalanceServiceApplication.class, args);
        System.out.println("Balance Service запущен!");
        System.out.println("Swagger UI: http://localhost:8084/api/balance/swagger-ui/index.html");
    }
}