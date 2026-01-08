package com.spavnit.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Catalog Service
 */
@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
        System.out.println("Catalog Service запущен!");
        System.out.println("Swagger UI: http://localhost:8085/api/catalog/swagger-ui/index.html");
    }
}