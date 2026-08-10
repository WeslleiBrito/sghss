package com.example.sghss.model;


import com.example.sghss.model.base.EntidadeBase;
import com.example.sghss.model.enums.TipoItemPrescricao;
import com.example.sghss.model.enums.ViaAdministracao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_item_prescricao")
@NoArgsConstructor
public class ItemPrescricao extends EntidadeBase {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "prescricao_id", nullable = false)
    private PrescricaoMedica prescricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_item", nullable = false, length = 30)
    private TipoItemPrescricao tipoItem;


    @Column(name = "descricao_item", nullable = false, length = 150)
    private String descricaoItem;


    @Column(name = "dosagem", length = 50)
    private String dosagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "via_administracao", length = 30)
    private ViaAdministracao viaAdministracao;

    @Column(name = "frequencia_horaria", nullable = false, length = 50)
    private String frequenciaHoraria;

    @Column(name = "instrucoes_diluicao_aplicacao")
    private String instrucoesDiluicaoAplicacao;

    @Column(name = "requer_dupla_checagem", nullable = false)
    private Boolean requerDuplaChecagem = false;
}
