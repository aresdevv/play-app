package com.platzi.play.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PlayApp API")
                        .description("API para gestión de películas y recomendaciones usando IA")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PlayApp Team")
                                .email("support@playapp.com")
                                .url("https://playapp.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8090")
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.playapp.com")
                                .description("Servidor de producción")
                ));
    }
}
