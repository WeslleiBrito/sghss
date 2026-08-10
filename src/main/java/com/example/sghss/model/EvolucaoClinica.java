package com.example.sghss.model;


import com.example.sghss.model.base.EntidadeBase;
import com.example.sghss.model.enums.EstadoClinico;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_evolucao_clinica")
@NoArgsConstructor
public class EvolucaoClinica extends EntidadeBase {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profissional_saude_id", nullable = false)
    private ProfissionalSaude autor;

    @Column(name = "data_hora_evolucao", nullable = false)
    private LocalDateTime dataHoraEvolucao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_clinico", nullable = false, length = 30)
    private EstadoClinico estadoClinico;

    @Column(name = "descricao_evolucao", nullable = false, columnDefinition = "TEXT")
    private String descricaoEvolucao;

    @Column(name = "conduta_adotada", columnDefinition = "TEXT")
    private String condutaAdotada;
}