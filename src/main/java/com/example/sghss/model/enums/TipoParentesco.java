package com.example.sghss.model.enums;

import lombok.Getter;

@Getter
public enum TipoParentesco {

    // Vínculos Diretos e Jurídicos
    CONJUGE_COMPANHEIRO("Cônjuge / Companheiro(a)"),
    PAI("Pai"),
    MAE("Mãe"),
    FILHO("Filho"),
    FILHA("Filha"),
    IRMAO("Irmão"),
    IRMA("Irmã"),

    // Vínculos Estendidos
    AVO("Avô / Avó"),
    NETO("Neto / Neta"),
    TIO("Tio"),
    TIA("Tia"),
    SOBRINHO("Sobrinho(a)"),
    PRIMO("Primo(a)"),

    // Vínculos Assistenciais e Sociais (Muito fortes no Home Care!)
    CUIDADOR("Cuidador(a) Profissional"),
    AMIGO("Amigo(a)"),
    VIZINHO("Vizinho(a)"),

    // Representação Legal e Exceções
    RESPONSAVEL_LEGAL("Responsável Legal / Tutor"),
    OUTROS("Outros");

    private final String descricao;

    TipoParentesco(String descricao) {
        this.descricao = descricao;
    }
}