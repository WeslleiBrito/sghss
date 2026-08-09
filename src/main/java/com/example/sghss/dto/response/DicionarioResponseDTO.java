package com.example.sghss.dto.response;

public record DicionarioResponseDTO(
        String valor,    // O que o React envia no POST (ex: "CONJUGE_COMPANHEIRO")
        String descricao // O que a Recepcionista lê na tela (ex: "Cônjuge / Companheiro(a)")
) {
}