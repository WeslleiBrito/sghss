package com.example.sghss.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PrescricaoMedicaCreateDTO(
        @NotNull(message = "O ID do prontuário é obrigatório.")
        UUID prontuarioId,

        String observacoesClinicas,

        @Valid
        @NotEmpty(message = "A prescrição deve conter pelo menos um item.")
        List<ItemPrescricaoCreateDTO> itens
) {}