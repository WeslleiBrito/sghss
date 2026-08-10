package com.example.sghss.model.base;


import com.example.sghss.model.interfaces.DadosPessoa;
import com.example.sghss.model.enums.TipoPessoa;
import com.example.sghss.model.valueobject.Contato;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_pessoa", discriminatorType = DiscriminatorType.STRING)
public abstract class Pessoa extends EntidadeBase {

    @ElementCollection
    private List<Contato> contatos;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", insertable = false, updatable = false)
    private TipoPessoa tipoPessoa;

    public abstract DadosPessoa getRepresentacaoDados();
}