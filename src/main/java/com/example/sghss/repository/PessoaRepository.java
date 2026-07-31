package com.example.sghss.repository;

import com.example.sghss.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaFisica, UUID> {

    // O Spring gera a query SQL automaticamente só lendo o nome do método!
    Optional<PessoaFisica> findByCpf(String cpf);
}