package com.spavnit.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Cart Service
 */
@SpringBootApplication
public class CartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartServiceApplication.class, args);
        System.out.println("Cart Service запущен!");
        System.out.println("Swagger UI: http://localhost:8086/api/cart/swagger-ui/index.html");
    }
}