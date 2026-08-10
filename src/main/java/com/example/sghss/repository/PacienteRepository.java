package com.example.sghss.repository;

import com.example.sghss.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    Optional<Paciente> findByCartaoSus(String cartaoSus);
    boolean existsByCartaoSus(String cartaoSus);

    Optional<Paciente> findByPessoaFisicaCpf(String cpf);

}