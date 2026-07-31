package com.example.sghss.dto.response;

import com.example.sghss.model.interfaces.DadosPessoa;
import com.example.sghss.model.enums.TipoPessoa;
import com.example.sghss.model.valueobject.Contato;

import java.util.List;
import java.util.UUID;

public record DadosPessoaFisica(
        UUID id,
        List<Contato> contatos,
        String nome,
        String cpf,
        TipoPessoa tipoPessoa
) implements DadosPessoa {}
