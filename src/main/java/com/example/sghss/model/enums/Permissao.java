package com.example.sghss.model.enums;


import lombok.Getter;

@Getter
public enum Permissao {

    // --- DOMÍNIO CLÍNICO (Exclusivo para Profissionais de Saúde e próprio Paciente) ---
    PRONTUARIO_LER("prontuario:ler"),
    PRONTUARIO_ESCREVER("prontuario:escrever"),
    EXAME_SOLICITAR("exame:solicitar"),
    EXAME_LAUDAR("exame:laudar"),

    // --- DOMÍNIO OPERACIONAL E RECEPÇÃO ---
    AGENDAMENTO_CRIAR("agendamento:criar"),
    AGENDAMENTO_CANCELAR("agendamento:cancelar"),
    CHECKIN_REALIZAR("checkin:realizar"),
    FLUXO_PORTARIA_LER("portaria:ler"), // Permissão enxuta para o Segurança ver quem entrou!

    // --- DOMÍNIO FINANCEIRO ---
    FATURAMENTO_GERENCIAR("faturamento:gerenciar"),
    CAIXA_OPERAR("caixa:operar"),

    // --- DOMÍNIO DE SISTEMA (Exclusivo para Administradores) ---
    USUARIO_CRIAR("usuario:criar"),
    USUARIO_GERENCIAR("usuario:gerenciar"),
    CONFIGURACAO_SISTEMA("sistema:configurar");

    private final String stringPermissao;

    Permissao(String stringPermissao) {
        this.stringPermissao = stringPermissao;
    }
}
