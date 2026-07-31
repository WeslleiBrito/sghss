package com.example.sghss.model.enums;

import lombok.Getter;

public enum StatusAgendamento {
    AGENDADO,               // Marcado pelo paciente no App ou pela recepção
    CONFIRMADO,             // Paciente confirmou presença (via WhatsApp / SMS / App)
    AGUARDANDO_ATENDIMENTO, // MÁGICA: A recepcionista fez o Check-in! O paciente está na sala de espera
    EM_ATENDIMENTO,         // O médico chamou o paciente no consultório
    CONCLUIDO,              // Consulta finalizada (gerou ou não evolução no prontuário)
    CANCELADO,              // Cancelado pelo paciente ou pela clínica
    FALTA                   // Paciente não compareceu (No-show)
}