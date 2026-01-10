package com.spavnit.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Order Service
 */
@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("Order Service запущен!");
        System.out.println("Swagger UI: http://localhost:8087/api/orders/swagger-ui/index.html");
    }
}