package com.example.sghss.model;


import com.example.sghss.model.base.EntidadeBase;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_prescricao_medica")
@NoArgsConstructor
public class PrescricaoMedica extends EntidadeBase {

    @Column(name = "numero_prescricao", nullable = false, unique = true, length = 30)
    private String numeroPrescricao; // Ex: "PRES-2026-8841"

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;

    // Quem assumiu a responsabilidade clínica? Exige CRM/COREN ativo!
    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private ProfissionalSaude medico;

    @Column(name = "data_hora_emissao", nullable = false)
    private LocalDateTime dataHoraEmissao = LocalDateTime.now();

    @Column(name = "data_hora_validade", nullable = false)
    private LocalDateTime dataHoraValidade; // Para internação, normalmente dataHoraEmissao.plusHours(24)

    // A validação legal que desenvolvemos na entidade ProfissionalSaude!
    @Column(name = "assinada_digitalmente_icp", nullable = false)
    private Boolean assinadaDigitalmenteIcp = false;

    @Column(name = "observacoes_clinicas", columnDefinition = "TEXT")
    private String observacoesClinicas;

    @OneToMany(mappedBy = "prescricao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ItemPrescricao> itens;

    // Método de negócio: Verifica se a prescrição ainda pode ser executada pela enfermagem
    public boolean isValidaParaExecucao() {
        return LocalDateTime.now().isBefore(this.dataHoraValidade);
    }
}
