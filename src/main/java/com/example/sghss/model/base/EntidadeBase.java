package com.example.sghss.model.base;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe abstrata base atualizada para utilizar UUID como chave primária.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class EntidadeBase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Padrão nativo do Hibernate 6 / JPA 3.1+
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

}