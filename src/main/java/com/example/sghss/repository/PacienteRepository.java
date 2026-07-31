package com.example.sghss.repository;

import com.example.sghss.model.Paciente;
import com.example.sghss.dto.response.RelatorioProducaoDietaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    Optional<Paciente> findByCartaoSus(String cartaoSus);
    boolean existsByCartaoSus(String cartaoSus);

    // Busca o paciente através do CPF da Pessoa Física vinculada [source: 9]
    Optional<Paciente> findByPessoaFisicaCpf(String cpf);

    // Query de agregação para a cozinha hospitalar que modelamos anteriormente [source: 9]
    @Query("""
        SELECT new com.example.sghss.dto.response.RelatorioProducaoDietaDTO(r, COUNT(p))
        FROM Paciente p JOIN p.restricoesAlimentares r
        WHERE p.ativo = true
        GROUP BY r
    """)
    List<RelatorioProducaoDietaDTO> gerarRelatorioProducaoCozinha();
}