package com.example.sghss.model;

import com.example.sghss.model.base.EntidadeBase;
import com.example.sghss.model.enums.CondicaoClinica;
import com.example.sghss.model.enums.RestricaoAlimentar;
import com.example.sghss.model.enums.TipoSanguineo;
import com.example.sghss.model.valueobject.ContatoEmergencia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_paciente")
@NoArgsConstructor
public class Paciente extends EntidadeBase {

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_fisica_id", nullable = false)
    private PessoaFisica pessoaFisica;

    @Column(name = "cartao_sus", unique = true, length = 15)
    private String cartaoSus;

    @Column(name = "data_cadastro_clinico", nullable = false)
    private LocalDate dataCadastroClinico = LocalDate.now();

    @Column(nullable = false)
    private Boolean ativo = true;

    // 1. Tipo Sanguíneo
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sanguineo", length = 15)
    private TipoSanguineo tipoSanguineo;

    // 2. Alergias Conhecidas
    @ElementCollection
    @CollectionTable(
            name = "tb_paciente_alergia",
            joinColumns = @JoinColumn(name = "paciente_id")
    )
    @Column(name = "alergia", length = 100)
    private List<String> alergiasConhecidas;

    // 3. Restrições Alimentares (Cozinha)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tb_paciente_restricao_alimentar",
            joinColumns = @JoinColumn(name = "paciente_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "restricao", nullable = false)
    private List<RestricaoAlimentar> restricoesAlimentares;

    // 4. Condições Clínicas
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tb_paciente_condicao_clinica",
            joinColumns = @JoinColumn(name = "paciente_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "condicao", nullable = false)
    private List<CondicaoClinica> condicoesClinicas;

    @Column(name = "anotacoes_gerais", length = 255)
    private String anotacoesGerais;

    @ElementCollection
    @CollectionTable(
            name = "tb_paciente_contato_emergencia",
            joinColumns = @JoinColumn(name = "paciente_id")
    )
    private List<ContatoEmergencia> contatosEmergencia;

    // Método utilitário de domínio
    public boolean necessitaAtencaoNutricional() {
        return this.restricoesAlimentares != null && !this.restricoesAlimentares.isEmpty();
    }
}