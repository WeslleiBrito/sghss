package com.example.sghss.model.valueobject;

import com.example.sghss.model.enums.TipoParentesco;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Embeddable
public class ContatoEmergencia {

    @Column(name = "nome_contato_emergencia", nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_parentesco", nullable = false, length = 30)
    private TipoParentesco parentesco;

    // A MÁGICA: Reutilizamos o Value Object Contato em vez de criar campos de telefone soltos!
    @Embedded
    private Contato contato;
}