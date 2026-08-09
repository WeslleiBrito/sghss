package com.example.sghss.repository;

import com.example.sghss.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, UUID> {

    @Query("""
        SELECT COUNT(e) > 0 FROM Escala e 
        WHERE e.colaborador.id = :colaboradorId 
        AND e.dataHoraInicio < :fim 
        AND e.dataHoraFim > :inicio
    """)
    boolean existeColisaoDeHorario(
            @Param("colaboradorId") UUID colaboradorId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    // O Spring gera o SQL automaticamente:
    // WHERE colaborador_id = ? AND data_hora_inicio > ? ORDER BY data_hora_inicio ASC
    List<Escala> findByColaboradorIdAndDataHoraInicioAfterOrderByDataHoraInicioAsc(UUID colaboradorId, LocalDateTime dataHora);
}
