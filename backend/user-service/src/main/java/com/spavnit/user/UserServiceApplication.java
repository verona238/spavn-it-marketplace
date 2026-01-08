package com.spavnit.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения User Service
 */
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("User Service запущен!");
        System.out.println("Swagger UI: http://localhost:8083/api/users/swagger-ui/index.html");
    }
}