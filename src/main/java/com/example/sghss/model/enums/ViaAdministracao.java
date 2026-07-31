package com.example.sghss.model.enums;

import lombok.Getter;

@Getter
public enum ViaAdministracao {
    ORAL("VO - Via Oral"),
    INTRAVENOSA("IV / EV - Intravenosa / Endovenosa"),
    INTRAMUSCULAR("IM - Intramuscular"),
    SUBCUTANEA("SC - Subcutânea"),
    INALATORIA("INAL - Inalatória / Nebulização"),
    ENTERAL("SNE / SNG - Sonda Enteral ou Gástrica"),
    TOPICA("TOP - Uso Tópico / Cutâneo"),
    OFTALMICA("OFT - Uso Oftálmico"),
    SUBLINGUAL("SL - Sublingual");

    private final String siglaDescricao;

    ViaAdministracao(String siglaDescricao) {
        this.siglaDescricao = siglaDescricao;
    }
}
