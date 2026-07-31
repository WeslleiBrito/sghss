package com.example.sghss.repository;

import com.example.sghss.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {

    Optional<Agendamento> findByCodigoAgendamento(String codigoAgendamento);

    List<Agendamento> findByPacienteIdOrderByDataHoraAgendadaDesc(UUID pacienteId);

    // 1. REAPROVEITADO E MELHORADO: Valida o Profissional direto (com ou sem escala) ignorando cancelados
    @Query("""
        SELECT COUNT(a) > 0 FROM Agendamento a 
         WHERE a.escala.colaborador.id = :profissionalId 
           AND a.dataHoraAgendada = :dataHora 
           AND a.statusAgendamento != com.example.sghss.model.enums.StatusAgendamento.CANCELADO
    """)
    boolean existeConflitoHorarioProfissional(
            @Param("profissionalId") UUID profissionalId,
            @Param("dataHora") LocalDateTime dataHora
    );

    // 2. NOVO: Blindagem para o Paciente não duplicar agendamentos no mesmo horário
    @Query("""
        SELECT COUNT(a) > 0 FROM Agendamento a 
        WHERE a.paciente.id = :pacienteId 
          AND a.dataHoraAgendada = :dataHora 
          AND a.statusAgendamento != com.example.sghss.model.enums.StatusAgendamento.CANCELADO
    """)
    boolean existeConflitoHorarioPaciente(
            @Param("pacienteId") UUID pacienteId,
            @Param("dataHora") LocalDateTime dataHora
    );

    @Query("""
        SELECT a.dataHoraAgendada FROM Agendamento a 
        WHERE a.escala.id = :escalaId 
          AND a.statusAgendamento != com.example.sghss.model.enums.StatusAgendamento.CANCELADO
    """)
    List<LocalDateTime> findHorariosOcupadosPorEscala(@Param("escalaId") UUID escalaId);

    List<Agendamento> findByEscalaColaboradorIdOrderByDataHoraAgendadaAsc(UUID profissionalId);

    List<Agendamento> findByDataHoraAgendadaBetweenOrderByDataHoraAgendadaAsc(LocalDateTime inicio, LocalDateTime fim);
}