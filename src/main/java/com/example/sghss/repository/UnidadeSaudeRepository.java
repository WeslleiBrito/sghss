package com.example.sghss.repository;

import com.example.sghss.model.base.UnidadeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, UUID> {

    // O Spring faz um JOIN automático: tb_unidade_saude -> tb_instituicao -> razao_social
    Optional<UnidadeSaude> findByInstituicaoRazaoSocial(String razaoSocial);

    boolean existsByInstituicaoRazaoSocial(String razaoSocial);

    // DICA: Se na sua PessoaJuridica você preferir usar o nome fantasia:
    Optional<UnidadeSaude> findByInstituicaoNomeFantasia(String nomeFantasia);
    boolean existsByInstituicaoNomeFantasia(String nomeFantasia);
}