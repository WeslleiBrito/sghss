package com.example.sghss.model;

import com.example.sghss.exception.BusinessException;
import com.example.sghss.model.base.EntidadeBase;
import com.example.sghss.model.enums.StatusAgendamento;
import com.example.sghss.model.enums.TipoAtendimento;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_agendamento")
public class Agendamento extends EntidadeBase {

    @Column(name = "codigo_agendamento", nullable = false, unique = true, length = 30)
    private String codigoAgendamento;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // --- CIRURGIA FEITA AQUI: Profissional e Unidade removidos! A Escala é a dona da verdade. ---
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "escala_id", nullable = false)
    private Escala escala;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atendimento", nullable = false, length = 30)
    private TipoAtendimento tipoAtendimento;

    @Column(name = "data_hora_agendada", nullable = false)
    private LocalDateTime dataHoraAgendada;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento", nullable = false, length = 30)
    private StatusAgendamento statusAgendamento = StatusAgendamento.AGENDADO;

    @Column(name = "data_hora_checkin")
    private LocalDateTime dataHoraCheckin;

    @Column(name = "data_hora_inicio_atendimento")
    private LocalDateTime dataHoraInicioAtendimento;

    @Column(name = "data_hora_fim_atendimento")
    private LocalDateTime dataHoraFimAtendimento;

    @Column(name = "motivo_cancelamento", length = 255)
    private String motivoCancelamento;

    @Column(name = "observacoes_recepcao", length = 255)
    private String observacoesRecepcao;


    // =========================================================================
    // --- MÉTODOS DE REGRA DE DOMÍNIO (MÁQUINA DE ESTADOS) ---
    // =========================================================================

    // 1. CONFIRMAÇÃO (O paciente respondeu SMS/WhatsApp ou o App confirmou)
    public void confirmar() {
        if (this.statusAgendamento != StatusAgendamento.AGENDADO) {
            throw new BusinessException("Apenas agendamentos no status AGENDADO podem ser confirmados.");
        }
        this.statusAgendamento = StatusAgendamento.CONFIRMADO;
    }

    // 2. CHECK-IN NA RECEPÇÃO (Atividade-Meio)
    public boolean canRealizarCheckIn() {
        return this.statusAgendamento == StatusAgendamento.AGENDADO ||
                this.statusAgendamento == StatusAgendamento.CONFIRMADO;
    }

    public void realizarCheckIn() {
        if (!canRealizarCheckIn()) {
            throw new BusinessException("O status atual (" + this.statusAgendamento + ") não permite realizar Check-in.");
        }
        this.statusAgendamento = StatusAgendamento.AGUARDANDO_ATENDIMENTO;
        this.dataHoraCheckin = LocalDateTime.now();
    }

    // 3. INÍCIO DO ATENDIMENTO MÉDICO (O Médico chamou no painel)
    public boolean canIniciarAtendimento() {
        // Blindagem: O médico só chama quem passou pela triagem/recepção!
        return this.statusAgendamento == StatusAgendamento.AGUARDANDO_ATENDIMENTO;
    }

    public void iniciarAtendimento() {
        if (!canIniciarAtendimento()) {
            throw new BusinessException("O paciente precisa realizar o Check-in na recepção antes de iniciar o atendimento.");
        }
        this.statusAgendamento = StatusAgendamento.EM_ATENDIMENTO;
        this.dataHoraInicioAtendimento = LocalDateTime.now();
    }

    // 4. CONCLUSÃO DO ATENDIMENTO (O Médico finalizou a consulta)
    public void concluir() {
        if (this.statusAgendamento != StatusAgendamento.EM_ATENDIMENTO) {
            throw new BusinessException("Não é possível concluir um atendimento que não foi iniciado.");
        }
        this.statusAgendamento = StatusAgendamento.CONCLUIDO;
        this.dataHoraFimAtendimento = LocalDateTime.now();
    }

    // 5. CANCELAMENTO (Pelo paciente, clínica ou por erro de agendamento)
    public void cancelar(String motivo) {
        // Não se pode cancelar algo que já está acontecendo ou já terminou!
        if (this.statusAgendamento == StatusAgendamento.EM_ATENDIMENTO ||
                this.statusAgendamento == StatusAgendamento.CONCLUIDO) {
            throw new BusinessException("Agendamentos em andamento ou já concluídos não podem ser cancelados.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new BusinessException("É obrigatório informar o motivo do cancelamento para auditoria.");
        }
        this.statusAgendamento = StatusAgendamento.CANCELADO;
        this.motivoCancelamento = motivo;
    }

    // 6. REGISTRO DE FALTA (No-Show)
    public void registrarFalta() {
        // Só toma falta quem estava agendado/confirmado e não apareceu no hospital
        if (this.statusAgendamento != StatusAgendamento.AGENDADO &&
                this.statusAgendamento != StatusAgendamento.CONFIRMADO) {
            throw new BusinessException("A falta só pode ser registrada para agendamentos pendentes de comparecimento.");
        }
        this.statusAgendamento = StatusAgendamento.FALTA;
    }
}