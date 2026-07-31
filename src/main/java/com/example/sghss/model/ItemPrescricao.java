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

    // Nome do fármaco, cuidado ou dieta (Ex: "Dipirona Sódica", "Aferir Glicemia Capilar")
    @Column(name = "descricao_item", nullable = false, length = 150)
    private String descricaoItem;

    // Específico para Medicamentos
    @Column(name = "dosagem", length = 50)
    private String dosagem; // Ex: "500mg (20 gotas)", "1 ampola (2ml)"

    @Enumerated(EnumType.STRING)
    @Column(name = "via_administracao", length = 30)
    private ViaAdministracao viaAdministracao;

    // Regra de tempo (Posologia)
    @Column(name = "frequencia_horaria", nullable = false, length = 50)
    private String frequenciaHoraria; // Ex: "De 6 em 6 horas (Q6H)", "1x ao dia (6h da manhã)", "ACM/SN (Se dor ou febre)"

    @Column(name = "instrucoes_diluicao_aplicacao", length = 255)
    private String instrucoesDiluicaoAplicacao; // Ex: "Diluir em 100ml de Soro Fisiológico 0,9% e correr em 30 min"

    @Column(name = "requer_dupla_checagem", nullable = false)
    private Boolean requerDuplaChecagem = false; // Essencial para drogas de alta vigilância (Insulina, Heparina, Sedativos)
}
