package com.example.sghss.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SGHSS - Sistema de Gestão Hospitalar e Saúde Suplementar",
                version = "v1.0",
                description = "API RESTful para gestão clínica, agendamento de consultas e prontuário eletrônico do paciente (PEP).",
                contact = @Contact(name = "Equipe de Desenvolvimento Back-end")
        ),

        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Insira o token JWT recebido no endpoint de login para testar as rotas protegidas."
)
public class OpenApiConfig {
}