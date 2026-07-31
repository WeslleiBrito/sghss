package com.example.sghss.model.base;

import com.example.sghss.model.PessoaFisica;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_colaborador")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_colaborador", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class Colaborador extends EntidadeBase {

    @ManyToOne(optional = false)
    @JoinColumn(name = "pessoa_fisica_id", nullable = false)
    private PessoaFisica pessoaFisica;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_lotacao_id", nullable = false)
    private UnidadeSaude unidadeLotacao;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    // REMOVEMOS O SET<PERFILACESSO> DAQUI! A SEGURANÇA NÃO É MAIS DO COLABORADOR.

    @Column(nullable = false)
    private Boolean ativo;
}