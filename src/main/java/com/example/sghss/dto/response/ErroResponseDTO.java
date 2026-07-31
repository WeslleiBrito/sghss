package com.example.sghss.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL) // Oculta o campo de validações se ele for nulo (ex: em erros 404 ou 500)
public record ErroResponseDTO(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos // Ex: {"cpf": "O CPF é obrigatório", "cartaoSus": "Formato inválido"}
) {}