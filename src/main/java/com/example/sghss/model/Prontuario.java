package com.example.sghss.model;


import com.example.sghss.model.base.EntidadeBase;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_prontuario")
@NoArgsConstructor
public class Prontuario extends EntidadeBase {

    // Código único de identificação hospitalar (Ex: "PEP-2026-00008912")
    @Column(name = "numero_prontuario", nullable = false, unique = true, length = 30)
    private String numeroProntuario;

    // Relação 1:1 -> Todo paciente tem um único prontuário eletrônico na instituição
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    // Pela resolução CFM nº 1.821/2007, prontuários eletrônicos nunca são deletados, apenas arquivados
    @Column(nullable = false)
    private Boolean ativo = true;

    // O HISTÓRICO DE PROGRESSÃO: Aqui conectamos aquela nossa classe EvolucaoClinica!
    // Usamos @OrderBy para que o JPA sempre traga o histórico do mais recente para o mais antigo
    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("dataHoraEvolucao DESC")
    @ToString.Exclude // Evita loops infinitos de impressão no Lombok
    private List<EvolucaoClinica> evolucoes;

    // --- FUTURAS EXPANSÕES DO PEP ---
    // @OneToMany(mappedBy = "prontuario")
    // private List<PrescricaoMedica> prescricoes;
    //
    // @OneToMany(mappedBy = "prontuario")
    // private List<ResultadoExame> exames;
}