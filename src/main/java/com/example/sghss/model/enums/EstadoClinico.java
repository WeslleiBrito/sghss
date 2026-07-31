package com.example.sghss.model.enums;


import lombok.Getter;

@Getter
public enum EstadoClinico {
    ESTAVEL("Estável"),
    INSPIRANDO_CUIDADOS("Inspirando Cuidados / Regular"),
    GRAVE("Grave"),
    CRITICO("Crítico / UTI"),
    EM_MELHORA("Em Melhora Clínica"),
    ALTA_MEDICA("Alta Médica"),
    OBITO("Óbito");

    private final String descricao;

    EstadoClinico(String descricao) {
        this.descricao = descricao;
    }
}
