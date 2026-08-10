package com.example.sghss.repository;

import com.example.sghss.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, UUID> {

    Optional<Especialidade> findByNome(String nome);

}