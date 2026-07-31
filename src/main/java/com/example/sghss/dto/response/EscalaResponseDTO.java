package com.example.sghss.dto.response;

import com.example.sghss.model.Escala;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaResponseDTO(
        UUID id,
        UUID profissionalSaudeId,
        String nomeProfissional,
        UUID unidadeSaudeId,
        String tipoAtividade,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim
) {
    public static EscalaResponseDTO fromEntity(Escala escala) {
        return new EscalaResponseDTO(
                escala.getId(),
                escala.getColaborador().getId(),
                escala.getColaborador().getPessoaFisica().getNome(),
                escala.getUnidadeSaude().getId(),
                escala.getTipoAtividade().name(),
                escala.getDataHoraInicio(),
                escala.getDataHoraFim()
        );
    }
}