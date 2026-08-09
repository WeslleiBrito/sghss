package com.example.sghss.dto.response;

import com.example.sghss.model.Escala;
import java.time.LocalDateTime;
import java.util.UUID;

public record EscalaResumoDTO(
        UUID id,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String tipoAtividade // Ex: AMBULATORIO, PLANTAO
) {
    public static EscalaResumoDTO fromEntity(Escala escala) {
        return new EscalaResumoDTO(
                escala.getId(),
                escala.getDataHoraInicio(),
                escala.getDataHoraFim(),
                escala.getTipoAtividade() != null ? escala.getTipoAtividade().name() : null
        );
    }
}