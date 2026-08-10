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


    @Column(name = "numero_prontuario", nullable = false, unique = true, length = 30)
    private String numeroProntuario;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;


    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("dataHoraEvolucao DESC")
    @ToString.Exclude
    private List<EvolucaoClinica> evolucoes;


}