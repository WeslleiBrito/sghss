package com.example.sghss.dto.response;

import java.time.LocalDateTime;

public record HorarioDisponivelDTO(
        LocalDateTime dataHora,
        String horaFormatada, // Ex: "08:30" (fácil para o front renderizar o botão!)
        boolean disponivel
) {}