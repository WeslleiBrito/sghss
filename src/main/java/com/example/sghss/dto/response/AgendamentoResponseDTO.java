package com.example.sghss.dto.response;

import com.example.sghss.model.Agendamento;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL) // Oculta no JSON campos de data que ainda estão nulos (ex: fim do atendimento)
public record AgendamentoResponseDTO(
        UUID id,
        String codigoAgendamento,
        UUID pacienteId,
        String nomePaciente,
        UUID profissionalSaudeId,
        String nomeProfissional,
        UUID unidadeSaudeId,
        String nomeUnidade,
        String tipoAtendimento,
        LocalDateTime dataHoraAgendada,
        String statusAgendamento,
        LocalDateTime dataHoraCheckin,
        LocalDateTime dataHoraInicioAtendimento,
        LocalDateTime dataHoraFimAtendimento,
        String observacoesRecepcao
) {
    // Método estático que converte a Entidade rica no DTO limpo para a API
    public static AgendamentoResponseDTO fromEntity(Agendamento agendamento) {
        return new AgendamentoResponseDTO(
                agendamento.getId(),
                agendamento.getCodigoAgendamento(),
                agendamento.getPaciente().getId(),
                agendamento.getPaciente().getPessoaFisica().getNome(),
                agendamento.getProfissionalSaude().getId(),
                agendamento.getProfissionalSaude().getPessoaFisica().getNome(),
                agendamento.getUnidadeSaude().getId(),
                agendamento.getUnidadeSaude().getInstituicao().getRazaoSocial(),
                agendamento.getTipoAtendimento() != null ? agendamento.getTipoAtendimento().name() : null,
                agendamento.getDataHoraAgendada(),
                agendamento.getStatusAgendamento() != null ? agendamento.getStatusAgendamento().name() : null,
                agendamento.getDataHoraCheckin(),
                agendamento.getDataHoraInicioAtendimento(),
                agendamento.getDataHoraFimAtendimento(),
                agendamento.getObservacoesRecepcao()
        );
    }
}