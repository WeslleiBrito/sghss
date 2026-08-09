package com.example.sghss.model.enums;

import lombok.Getter;

@Getter
public enum TipoContato {
    TELEFONE_COMERCIAL(1, "Telefone Comercial"),
    TELEFONE_EMERGENCIA(2, "Telefone de Emergência"),
    WHATSAPP(3, "WhatsApp"),
    EMAIL_CORPORATIVO(4, "E-mail Corporativo"),
    EMAIL_FATURAMENTO(5, "E-mail de Faturamento"),
    INSTAGRAM(6, "Instagram"),
    WEBSITE(7, "Website Oficial"),
    LINKEDIN(8, "LinkedIn");

    private final Integer codigo;
    private final String descricao;

    TipoContato(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}