package com.example.sghss.model;

import com.example.sghss.model.base.Pessoa;
import com.example.sghss.dto.response.DadosPessoaJuridica;
import com.example.sghss.model.enums.TipoPessoa;
import com.example.sghss.model.valueobject.Endereco;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_pessoa_juridica")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "pessoa_id")
@DiscriminatorValue("JURIDICA")
@NoArgsConstructor
public abstract class PessoaJuridica extends Pessoa {

    @Column(name = "razao_social", length = 150)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;

    @Column(unique = true, length = 14)
    private String cnpj;

    @Embedded
    private Endereco endereco;

    @Override
    public DadosPessoaJuridica getRepresentacaoDados() {
        return new DadosPessoaJuridica(
                this.getId(),
                this.getContatos(),
                this.cnpj,
                TipoPessoa.JURIDICA,
                this.razaoSocial,
                this.nomeFantasia
        );
    }
}