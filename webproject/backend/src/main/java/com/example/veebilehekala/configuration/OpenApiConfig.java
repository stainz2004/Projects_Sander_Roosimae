package com.example.veebilehekala.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fishing equipment store API")
                        .version("1.0")
                        .description("REST API for fishing equipment Store Application")
                        .contact(new Contact()
                                .name("FIISH E-Store Team")
                                .email("SanderR999@gmail.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080")
                                .description("Local"),
                        new Server().url("https://fiish.ddns.net/")
                                .description("Prod")
                ));
    }
}

