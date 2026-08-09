package com.example.sghss.dto.response;

import com.example.sghss.model.Agendamento;
import com.example.sghss.model.Especialidade;
import com.example.sghss.model.ProfissionalSaude;
import com.example.sghss.model.base.Colaborador;
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
        String especialidade,
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

                // NAVEGANDO PELA ESCALA:
                agendamento.getEscala().getColaborador().getId(),
                agendamento.getEscala().getColaborador().getPessoaFisica().getNome(),
                agendamento.getEspecialidade().getNome(),
                agendamento.getEscala().getUnidadeSaude().getId(),
                agendamento.getEscala().getUnidadeSaude().getInstituicao().getRazaoSocial(),

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