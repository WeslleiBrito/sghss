package com.example.sghss.dto.request;

import com.example.sghss.model.enums.EstadoClinico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record EvolucaoClinicaCreateDTO(
        @NotNull(message = "O ID do prontuário é obrigatório.")
        UUID prontuarioId,

        @NotNull(message = "O estado clínico atual é obrigatório.")
        EstadoClinico estadoClinico,

        @NotBlank(message = "A descrição detalhada da evolução clínica é obrigatória.")
        String descricaoEvolucao,

        String condutaAdotada
) {}