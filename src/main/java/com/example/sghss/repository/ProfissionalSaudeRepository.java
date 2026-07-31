package com.example.sghss.repository;

import com.example.sghss.model.ProfissionalSaude;
import com.example.sghss.model.enums.TipoConselho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, UUID> {


    Optional<ProfissionalSaude> findByNumeroConselho(String numeroConselho);


    Optional<ProfissionalSaude> findByNumeroConselhoAndTipoConselhoAndUfConselho(
            String numeroConselho,
            TipoConselho tipoConselho,
            String ufConselho
    );
}