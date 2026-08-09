package com.example.sghss.model;

import com.example.sghss.model.base.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_log_auditoria")
public class LogAuditoria extends EntidadeBase {

    @Column(nullable = false, updatable = false)
    private String usuarioLogin;

    @Column(nullable = false, updatable = false)
    private String endpointAcessado;

    @Column(length = 500, updatable = false)
    private String parametrosBuscados;

    @Column(nullable = false, updatable = false)
    private String ipOrigem;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHoraAcesso = LocalDateTime.now();
}