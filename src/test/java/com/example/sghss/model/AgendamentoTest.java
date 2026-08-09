package com.example.sghss.model;

import com.example.sghss.exception.BusinessException;
import com.example.sghss.model.enums.StatusAgendamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgendamentoTest {

    @Test
    @DisplayName("Deve transitar status para AGUARDANDO_ATENDIMENTO após Check-in válido")
    void deveFazerCheckInComSucesso() {
        Agendamento agendamento = new Agendamento();
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);

        agendamento.realizarCheckIn();

        assertEquals(StatusAgendamento.AGUARDANDO_ATENDIMENTO, agendamento.getStatusAgendamento());
        assertNotNull(agendamento.getDataHoraCheckin());
    }

    @Test
    @DisplayName("Não deve permitir que o médico inicie o atendimento antes do Check-in na recepção")
    void naoDeveIniciarAtendimentoSemCheckIn() {
        Agendamento agendamento = new Agendamento();
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO); // Ele ainda não fez check-in!

        BusinessException exception = assertThrows(BusinessException.class, agendamento::iniciarAtendimento);

        assertEquals("O paciente precisa realizar o Check-in na recepção antes de iniciar o atendimento.", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve permitir cancelar um agendamento que já está em andamento")
    void naoDeveCancelarAgendamentoEmAndamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setStatusAgendamento(StatusAgendamento.EM_ATENDIMENTO);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            agendamento.cancelar("Paciente foi embora da sala");
        });

        assertEquals("Agendamentos em andamento ou já concluídos não podem ser cancelados.", exception.getMessage());
    }
}