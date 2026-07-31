package com.example.sghss.dto.response;

import com.example.sghss.model.PessoaFisica;
import com.example.sghss.model.valueobject.Contato;
import com.example.sghss.model.valueobject.Endereco;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PessoaResponseDTO(
        UUID id,
        String nome,
                String cpf,
                LocalDate dataNascimento,
                List<Contato> contatos,
        List<Endereco> enderecos
) {
    public PessoaResponseDTO(PessoaFisica pessoa) {
        this(
                pessoa.getId(),
        pessoa.getNome(),
        pessoa.getCpf(),
        pessoa.getDataNascimento(),
        pessoa.getContatos(),
                pessoa.getEnderecos()
        );
    }
}