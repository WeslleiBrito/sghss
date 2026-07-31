package com.example.sghss.dto.response;

import java.util.Set;

public record TokenResponseDTO(
        String token,
        String tipo,
        String login,
        Set<String> perfis
) {}