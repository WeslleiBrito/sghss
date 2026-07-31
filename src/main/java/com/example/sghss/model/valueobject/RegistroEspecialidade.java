package com.example.sghss.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RegistroEspecialidade {
    private String nomeEspecialidade; // Ex: Cardiologia, Enfermagem Obstétrica
    private String rqe; // Registro de Qualificação de Especialidade (Essencial para médicos!)
}