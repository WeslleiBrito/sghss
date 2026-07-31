package com.example.sghss.model.interfaces;

import com.example.sghss.model.enums.TipoPessoa;
import com.example.sghss.model.valueobject.Contato;

import java.util.List;
import java.util.UUID;

public interface DadosPessoa {
    UUID id();
    List<Contato> contatos();
    TipoPessoa tipoPessoa();
}
