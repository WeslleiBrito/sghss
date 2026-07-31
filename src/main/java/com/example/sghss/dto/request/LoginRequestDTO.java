package com.example.sghss.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O login não pode estar em branco.")
        String login,

        @NotBlank(message = "A senha não pode estar em branco.")
        String senha
) {}