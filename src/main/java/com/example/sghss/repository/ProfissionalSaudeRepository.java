package com.example.sghss.repository;

import com.example.sghss.model.ProfissionalSaude;
import com.example.sghss.model.enums.TipoConselho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, UUID> {

    Optional<ProfissionalSaude> findByPessoaFisicaId(UUID pessoaFisicaId);

    @Query("SELECT DISTINCT p FROM ProfissionalSaude p " +
            "INNER JOIN Escala e ON e.colaborador.id = p.id " +
            "WHERE p.ativo = true AND e.dataHoraInicio > CURRENT_TIMESTAMP")
    List<ProfissionalSaude> findProfissionaisComEscalaFutura();


    @Query("SELECT DISTINCT p FROM ProfissionalSaude p " +
            "INNER JOIN Escala e ON e.colaborador.id = p.id " +
            "JOIN p.especialidades esp " +
            "WHERE p.ativo = true " +
            "AND e.dataHoraInicio > CURRENT_TIMESTAMP " +
            "AND esp.id = :especialidadeId")
    List<ProfissionalSaude> findPorEspecialidadeComEscalaFutura(@Param("especialidadeId") UUID especialidadeId);
}