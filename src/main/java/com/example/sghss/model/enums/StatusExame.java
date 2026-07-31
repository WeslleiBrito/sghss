package com.example.sghss.model.enums;


public enum StatusExame {
    SOLICITADO,       // O médico pediu na consulta
    EM_COLETA,        // O paciente está na sala de coleta / coleta domiciliar Home Care
    EM_ANALISE,       // O laboratório recebeu a amostra ou a imagem está sendo avaliada
    LAUDADO,          // O laudo foi assinado e está disponível para o médico
    CANCELADO
}
