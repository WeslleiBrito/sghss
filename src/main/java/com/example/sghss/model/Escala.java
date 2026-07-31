package com.example.sghss.model;



import com.example.sghss.model.base.Colaborador;
import com.example.sghss.model.base.EntidadeBase;
import com.example.sghss.model.base.UnidadeSaude;
import com.example.sghss.model.enums.TipoAtividade;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_escala")
@NoArgsConstructor
public class Escala extends EntidadeBase {

    @ManyToOne(optional = false)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atividade", nullable = false, length = 30)
    private TipoAtividade tipoAtividade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_saude_id", nullable = false)
    private UnidadeSaude unidadeSaude; // Onde ele vai estar fisicamente

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

}