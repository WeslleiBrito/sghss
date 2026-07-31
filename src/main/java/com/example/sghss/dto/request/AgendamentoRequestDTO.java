package com.example.sghss.dto.request;


import com.example.sghss.model.enums.OrigemAgendamento;

import java.time.LocalDateTime;
import java.util.UUID;

// O que o Front-end envia para a API
public record AgendamentoRequestDTO(
        UUID pacienteId,
        UUID escalaId,
        LocalDateTime dataHoraConsulta,
        OrigemAgendamento origem
) {}