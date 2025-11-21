package com.uniminuto.clinica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API REST.
 * Proporciona una interfaz web interactiva para explorar y probar los endpoints.
 * 
 * @author Giovanni Mora Jaimes
 * @version 1.0
 * @since 2025-11-21
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura la documentación OpenAPI para la aplicación.
     * Define información general, servidores, y esquema de seguridad JWT.
     * 
     * @return Objeto OpenAPI configurado con toda la información de la API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Sistema de Gestión Clínica")
                        .version("1.0.0")
                        .description("API REST para la gestión de una clínica médica. " +
                                "Incluye módulos de autenticación, auditoría, gestión de pacientes, " +
                                "médicos, citas, medicamentos y recuperación de contraseña.")
                        .contact(new Contact()
                                .name("Giovanni Mora Jaimes")
                                .email("gmora@uniminuto.edu")
                                .url("https://github.com/giovannimora0527"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8000/clinica/v1")
                                .description("Servidor de desarrollo local"),
                        new Server()
                                .url("https://api-clinica.uniminuto.edu/v1")
                                .description("Servidor de producción")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtenido del endpoint /auth/login. " +
                                        "Incluir en el header: Authorization: Bearer {token}")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
