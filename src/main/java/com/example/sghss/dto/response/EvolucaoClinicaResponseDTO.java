package com.example.sghss.dto.response;

import com.example.sghss.model.enums.EstadoClinico;
import java.time.LocalDateTime;
import java.util.UUID;

public record EvolucaoClinicaResponseDTO(
        UUID id,
        String autorNome,
        String autorRegistroConselho, // Ex: "CRM/BA 123456" [source: 9]
        LocalDateTime dataHoraEvolucao,
        EstadoClinico estadoClinico,
        String descricaoEvolucao,
        String condutaAdotada
) {}