package com.spavnit.cart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация OpenAPI (Swagger)
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI cartServiceAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8086/api/cart");
        server.setDescription("Cart Service Local");

        Contact contact = new Contact();
        contact.setName("SpavnIT Team");
        contact.setEmail("support@spavnit.com");

        Info info = new Info()
                .title("Cart Service API")
                .version("1.0.0")
                .description("API для управления корзиной покупок SpavnIT Marketplace. " +
                        "Пользователи могут добавлять товары в корзину, изменять количество, " +
                        "удалять товары и просматривать содержимое корзины.")
                .contact(contact);

        // Настройка Bearer Authentication для Swagger
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme));
    }
}