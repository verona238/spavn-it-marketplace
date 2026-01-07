package com.spavnit.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
    public OpenAPI notificationServiceAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8082/api/notifications");
        server.setDescription("Notification Service Local");

        Contact contact = new Contact();
        contact.setName("SpavnIT Team");
        contact.setEmail("support@spavnit.com");

        Info info = new Info()
                .title("Notification Service API")
                .version("1.0.0")
                .description("API для отправки уведомлений (email) пользователям SpavnIT Marketplace")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}