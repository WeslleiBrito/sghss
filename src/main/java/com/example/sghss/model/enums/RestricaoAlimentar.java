package com.example.sghss.model.enums;


import lombok.Getter;

@Getter
public enum RestricaoAlimentar {
    // Dietas por Patologia
    DIABETICA_ZERO_ACUCAR("Dieta Diabética (Zero Açúcar)"),
    HIPOSODICA("Dieta Hipossódica (Baixo Sódio/Sal)"),
    HIPOPROTEICA("Dieta Hipoproteica (Restrição de Proteínas)"),

    // Intolerâncias e Alergias Digestivas
    SEM_LACTOSE("Zero Lactose"),
    SEM_GLUTEN("Zero Glúten / Celíaco"),
    ALERGIA_FRUTOS_MAR("Alergia a Frutos do Mar"),
    ALERGIA_AMENDOIM("Alergia a Amendoim / Castanhas"),

    // Consistência e Logística de Ingestão (Crucial para a copa hospitalar!)
    DIETA_PASTOSA("Dieta Pastosa / Liquidificada"),
    SONDA_ENTERAL("Alimentação por Sonda Enteral / Gástrica"),
    JEJUM_ABSOLUTO("Jejum Absoluto (Pré/Pós-Operatório)");

    private final String descricao;

    RestricaoAlimentar(String descricao) {
        this.descricao = descricao;
    }
}