package com.example.sghss.model.enums;

import lombok.Getter;

@Getter
public enum TipoAtividade {
    AMBULATORIO, // Clínica
    PLANTAO,     // Hospital (Pronto Socorro/UTI)
    CIRURGIA,    // Hospital (Centro Cirúrgico)
    RESGATE,     // Home Care (Ambulância)
    FOLGA,       // Indisponível
    ALMOCO       // Indisponível
}