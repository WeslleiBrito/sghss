package com.example.sghss.model.enums;

import lombok.Getter;

@Getter
public enum CondicaoClinica {
    PORTADOR_MARCAPASSO("Portador de Marca-passo"),
    INSUFICIENCIA_RENAL("Insuficiência Renal Crônica"),
    HIPERTENSAO_GRAVE("Hipertensão Arterial Grave"),
    IMUNOSSUPRIMIDO("Paciente Imunossuprimido"),
    GESTANTE_ALTO_RISCO("Gestante de Alto Risco"),
    MOBILIDADE_REDUZIDA("Mobilidade Reduzida / Acamado"),
    ISOLAMENTO_INFECCIOSO("Isolamento por Infecção");

    private final String descricao;

    CondicaoClinica(String descricao) {
        this.descricao = descricao;
    }
}