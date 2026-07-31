package com.example.sghss.model;


import com.example.sghss.model.base.EntidadeBase;
import com.example.sghss.model.enums.StatusExame;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_resultado_exame")
@NoArgsConstructor
public class ResultadoExame extends EntidadeBase {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_solicitante_id", nullable = false)
    private ProfissionalSaude medicoSolicitante;

    // Médico ou Biomédico responsável pela liberação do laudo (Pode ser null enquanto o exame estiver EM_ANALISE)
    @ManyToOne
    @JoinColumn(name = "responsavel_laudo_id")
    private ProfissionalSaude responsavelLaudo;

    @Column(name = "codigo_solicitacao", nullable = false, unique = true, length = 30)
    private String codigoSolicitacao; // Ex: "EXM-2026-1092"

    @Column(name = "nome_exame", nullable = false, length = 150)
    private String nomeExame; // Ex: "Hemograma Completo", "Radiografia de Tórax PA/Perfil"

    @Column(name = "tipo_exame", nullable = false, length = 50)
    private String tipoExame; // Ex: LABORATORIAL, IMAGEM, ANATOMOPATOLOGICO, CARDIOLOGICO

    @Enumerated(EnumType.STRING)
    @Column(name = "status_exame", nullable = false, length = 20)
    private StatusExame statusExame = StatusExame.SOLICITADO;

    @Column(name = "data_hora_solicitacao", nullable = false)
    private LocalDateTime dataHoraSolicitacao = LocalDateTime.now();

    @Column(name = "data_hora_liberacao_laudo")
    private LocalDateTime dataHoraLiberacaoLaudo;

    @Column(name = "is_urgente", nullable = false)
    private Boolean isUrgente = false;

    // O texto completo do laudo médico / biomédico
    @Column(name = "conclusao_laudo", columnDefinition = "TEXT")
    private String conclusaoLaudo;

    // Específico para exames laboratoriais: os números de corte (Ex: "Leucócitos: 8.500 /mm³ | Ref: 4.000 a 10.000")
    @Column(name = "valores_referencia_obtidos", columnDefinition = "TEXT")
    private String valoresReferenciaObtidos;

    // Chave para localizar o PDF do laudo ou a imagem DICOM no Amazon S3 / PACS
    @Column(name = "url_arquivo_anexo", length = 500)
    private String urlArquivoAnexo;

    // Método utilitário que a central de alertas do hospital consome para notificar o médico
    public boolean isProntoParaVisualizacao() {
        return this.statusExame == StatusExame.LAUDADO && this.conclusaoLaudo != null;
    }
}