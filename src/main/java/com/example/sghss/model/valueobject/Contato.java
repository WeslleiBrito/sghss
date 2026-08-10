package com.example.sghss.model.valueobject;


import com.example.sghss.model.enums.TipoContato;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Embeddable
public class Contato {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contato", nullable = false, length = 30)
    private TipoContato tipo;

    @Column(nullable = false, length = 150)
    private String valor;

    @Column(length = 100)
    private String observacao; 
}
