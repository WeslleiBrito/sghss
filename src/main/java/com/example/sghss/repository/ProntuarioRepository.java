package com.example.sghss.repository;

import com.example.sghss.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, UUID> {

    Optional<Prontuario> findByPacienteId(UUID pacienteId);
}