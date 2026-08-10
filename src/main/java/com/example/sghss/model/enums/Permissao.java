package com.example.sghss.model.enums;


import lombok.Getter;

@Getter
public enum Permissao {

    PRONTUARIO_LER("prontuario:ler"),
    PRONTUARIO_ESCREVER("prontuario:escrever"),
    EXAME_SOLICITAR("exame:solicitar"),
    EXAME_LAUDAR("exame:laudar"),


    AGENDAMENTO_CRIAR("agendamento:criar"),
    AGENDAMENTO_CANCELAR("agendamento:cancelar"),
    CHECKIN_REALIZAR("checkin:realizar"),
    FLUXO_PORTARIA_LER("portaria:ler"),


    FATURAMENTO_GERENCIAR("faturamento:gerenciar"),
    CAIXA_OPERAR("caixa:operar"),


    USUARIO_CRIAR("usuario:criar"),
    USUARIO_GERENCIAR("usuario:gerenciar"),
    CONFIGURACAO_SISTEMA("sistema:configurar");

    private final String stringPermissao;

    Permissao(String stringPermissao) {
        this.stringPermissao = stringPermissao;
    }
}
