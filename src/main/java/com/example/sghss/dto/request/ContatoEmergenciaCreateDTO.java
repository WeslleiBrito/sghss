package com.example.sghss.dto.request;

import com.example.sghss.model.enums.TipoParentesco;
import com.example.sghss.model.valueobject.ContatoEmergencia;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoEmergenciaCreateDTO(
        @NotBlank(message = "O nome do contato de emergência é obrigatório.")
        String nome,

        @NotNull(message = "O grau de parentesco é obrigatório.")
        TipoParentesco parentesco,

        // @Valid faz o Spring validar o tipo (telefone, whatsapp) e o número dentro de ContatoCreateDTO!
        @Valid
        @NotNull(message = "Os dados de comunicação do contato são obrigatórios.")
        ContatoCreateDTO contato
) {
    public ContatoEmergencia toEntity() {
        ContatoEmergencia ce = new ContatoEmergencia();
        ce.setNome(this.nome());
        ce.setParentesco(this.parentesco());
        ce.setContato(this.contato().toEntity()); // Chama o .toEntity() do ContatoCreateDTO!
        return ce;
    }
}