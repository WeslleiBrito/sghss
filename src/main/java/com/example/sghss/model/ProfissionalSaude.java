package com.example.sghss.model;


import com.example.sghss.model.base.Colaborador;
import com.example.sghss.model.enums.TipoConselho;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_profissional_saude")
@PrimaryKeyJoinColumn(name = "colaborador_id")
@DiscriminatorValue("SAUDE")
@NoArgsConstructor
public class ProfissionalSaude extends Colaborador {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conselho", nullable = false, length = 10)
    private TipoConselho tipoConselho;

    @Column(name = "numero_conselho", nullable = false, length = 20)
    private String numeroConselho;

    @Column(name = "uf_conselho", nullable = false, length = 2)
    private String ufConselho; // Ex: BA, SP, RJ

    // Um médico pode ser Cardiologista E Intensivista (UTI) ao mesmo tempo!
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_profissional_especialidade", // Nome da tabela que o Hibernate vai criar no banco
            joinColumns = @JoinColumn(name = "profissional_id"), // A chave estrangeira para o médico
            inverseJoinColumns = @JoinColumn(name = "especialidade_id") // A chave estrangeira para a especialidade
    )
    private List<Especialidade> especialidades = new ArrayList<>();

    // Regras de Atendimento
    @Column(name = "permite_telemedicina", nullable = false)
    private Boolean permiteTelemedicina = false;

    @Column(name = "possui_assinatura_digital_icp", nullable = false)
    private Boolean possuiAssinaturaDigitalIcp = false; // Necessário para receita eletrônica válida

    @Column(name = "tempo_medio_consulta_minutos", nullable = false)
    private Integer tempoMedioConsultaMinutos = 30; // Ajuda a recepção a calcular a agenda

    // Método utilitário para exibição no front-end ou em receituários
    public String getIdentificacaoProfissional() {
        return String.format("%s/%s %s", this.tipoConselho, this.ufConselho, this.numeroConselho);
    }
}