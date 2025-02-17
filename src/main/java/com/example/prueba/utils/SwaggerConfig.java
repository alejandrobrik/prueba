package com.example.prueba.utils;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuarios y Autenticación")
                        .version("1.0")
                        .description("Documentación de la API para la prueba técnica en Spring Boot")
                        .contact(new Contact()
                                .name("Alejandro Angulo")
                                .email("jairo.angulo2017@uteq.edu.ec")
                                .url("https://github.com/alejandrobrik/prueba")));
    }
}
