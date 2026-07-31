package com.example.sghss.dto.request;

import com.example.sghss.model.enums.TipoAtendimento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record AgendamentoCreateDTO(
        @NotNull(message = "O ID do paciente é obrigatório.")
        UUID pacienteId,

        @NotNull(message = "O ID da escala (agenda) é obrigatório.")
        UUID escalaId,

        @NotNull(message = "A data e hora do agendamento são obrigatórias.")
        @Future(message = "O agendamento deve ser realizado para o futuro.")
        LocalDateTime dataHoraAgendada,

        @NotNull(message = "O tipo de atendimento é obrigatório.")
        TipoAtendimento tipoAtendimento,

        String observacoesRecepcao
) {}