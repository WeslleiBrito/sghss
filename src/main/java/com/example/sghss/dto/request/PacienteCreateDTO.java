package com.example.sghss.dto.request;

import com.example.sghss.model.Paciente;
import com.example.sghss.model.PessoaFisica;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public record PacienteCreateDTO(
        // A MÁGICA DO CADASTRO ÚNICO: Valida Nome, CPF, Data, Contatos e Endereços civis em cascata!
        @Valid
        @NotNull(message = "Os dados de identificação civil (Pessoa Física) são obrigatórios.")
        PessoaFisicaCreateDTO pessoaFisica,

        @NotBlank(message = "O Cartão SUS é obrigatório.")
        @Pattern(regexp = "\\d{15}", message = "O Cartão SUS deve conter exatamente 15 dígitos numéricos.")
        String cartaoSus,

        @Valid
        List<ContatoEmergenciaCreateDTO> contatosEmergencia,

        String anotacoesGerais
) {
        // Como o Paciente PRECISA saber quem é a sua Pessoa Física, nós passamos ela como parâmetro no método:
        public Paciente toEntity(PessoaFisica pessoaFisicaGerenciada) {
                Paciente paciente = new Paciente();
                paciente.setPessoaFisica(pessoaFisicaGerenciada);
                paciente.setCartaoSus(this.cartaoSus());
                paciente.setAnotacoesGerais(this.anotacoesGerais());

                // Mapeamento limpo da lista de contatos de urgência
                if (this.contatosEmergencia() != null && !this.contatosEmergencia().isEmpty()) {
                        paciente.setContatosEmergencia(
                                this.contatosEmergencia().stream()
                                        .map(ContatoEmergenciaCreateDTO::toEntity)
                                        .toList()
                        );
                } else {
                        paciente.setContatosEmergencia(new ArrayList<>());
                }

                // BLINDAGEM CLÍNICA: Nascem rigorosamente vazios à espera da triagem ou consulta!
                paciente.setTipoSanguineo(null);
                paciente.setAlergiasConhecidas(new ArrayList<>());
                paciente.setRestricoesAlimentares(new ArrayList<>());
                paciente.setCondicoesClinicas(new ArrayList<>());

                return paciente;
        }
}