package com.example.sghss.dto.request;

import com.example.sghss.model.enums.TipoContato;
import com.example.sghss.model.valueobject.Contato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoCreateDTO(
        @NotNull(message = "O tipo de contato é obrigatório (Ex: TELEFONE_COMERCIAL, WHATSAPP, EMAIL_CORPORATIVO).")
        TipoContato tipo,

        @NotBlank(message = "O valor do contato (número, e-mail, etc.) não pode estar em branco.")
        String valor,

        String observacao
) {
    // O próprio DTO assume a responsabilidade de instanciar o Value Object [source: 8]
    public Contato toEntity() {
        Contato contato = new Contato();
        contato.setTipo(this.tipo());
        contato.setValor(this.valor());
        contato.setObservacao(this.observacao());
        return contato;
    }
}