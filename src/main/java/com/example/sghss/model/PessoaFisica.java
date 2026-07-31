package com.example.sghss.model;

import com.example.sghss.model.base.Pessoa;
import com.example.sghss.dto.response.DadosPessoaFisica;
import com.example.sghss.model.enums.TipoPessoa;
import com.example.sghss.model.valueobject.Endereco;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_pessoa_fisica")
@PrimaryKeyJoinColumn(name = "pessoa_id")
@DiscriminatorValue("FISICA")
@NoArgsConstructor
public class PessoaFisica extends Pessoa {

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @ElementCollection
    @CollectionTable(
            name = "tb_pessoa_fisica_endereco",
            joinColumns = @JoinColumn(name = "pessoa_fisica_id")
    )
    private List<Endereco> enderecos;


    @Override
    public DadosPessoaFisica getRepresentacaoDados() {
        return new DadosPessoaFisica(
                this.getId(),
                this.getContatos(),
                this.nome,
                this.cpf,
                TipoPessoa.FISICA
        );
    }
}