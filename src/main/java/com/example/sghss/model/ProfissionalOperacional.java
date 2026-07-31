package com.example.sghss.model;


import com.example.sghss.model.base.Colaborador;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_profissional_operacional")
@PrimaryKeyJoinColumn(name = "colaborador_id")
@DiscriminatorValue("OPERACIONAL")
@NoArgsConstructor
public class ProfissionalOperacional extends Colaborador {

    @Column(nullable = false, length = 100)
    private String cargo; // Ex: Recepcionista, Segurança Patrimonial, Motorista de Ambulância, Maqueiro

    @Column(nullable = false, length = 100)
    private String setor; // Ex: Pronto Socorro, Portaria Central, Transporte/Resgate, Faturamento

    // --- Controles para Recepção e Administração ---
    @Column(name = "possui_acesso_caixa", nullable = false)
    private Boolean possuiAcessoCaixa = false; // Pode receber pagamentos de consultas particulares?

    @Column(name = "pode_cancelar_agendamento", nullable = false)
    private Boolean podeCancelarAgendamento = false; // Exige permissão de supervisor?

    // --- Controles Específicos para Resgate (Motoristas) e Segurança ---
    @Column(name = "numero_cnh", length = 20)
    private String numeroCnh;

    @Column(name = "categoria_cnh", length = 5)
    private String categoriaCnh; // Motorista de ambulância exige CNH categoria D ou E com curso EAR

    @Column(name = "data_validade_cnh")
    private LocalDate dataValidadeCnh;

    @Column(name = "validade_curso_reciclagem")
    private LocalDate validadeCursoReciclagem; // Essencial para vigilantes (CNV) e condutores de emergência

    // Método utilitário para validar se o motorista ou segurança está apto para o plantão de resgate
    public boolean isCertificacaoOperacionalValida() {
        if (validadeCursoReciclagem == null) return true; // Se o cargo não exige curso, está apto
        return validadeCursoReciclagem.isAfter(LocalDate.now());
    }
}