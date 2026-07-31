package com.example.sghss.model.base;


import com.example.sghss.model.Instituicao;
import com.example.sghss.model.enums.TipoUnidade;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_unidade_saude")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_unidade", discriminatorType = DiscriminatorType.STRING)
public abstract class UnidadeSaude extends Pessoa {

    @Column(name = "status_operacao", nullable = false)
    private Boolean statusOperacao;

    // É aqui que a Unidade diz a qual Holding/Instituição ela pertence!
    @ManyToOne(optional = false)
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;

    public abstract boolean consultarDisponibilidade();

    @Transient
    public abstract TipoUnidade getTipoUnidade();
}