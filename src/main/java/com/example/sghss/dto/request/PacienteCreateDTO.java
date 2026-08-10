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

        public Paciente toEntity(PessoaFisica pessoaFisicaGerenciada) {
                Paciente paciente = new Paciente();
                paciente.setPessoaFisica(pessoaFisicaGerenciada);
                paciente.setCartaoSus(this.cartaoSus());
                paciente.setAnotacoesGerais(this.anotacoesGerais());

                if (this.contatosEmergencia() != null && !this.contatosEmergencia().isEmpty()) {
                        paciente.setContatosEmergencia(
                                this.contatosEmergencia().stream()
                                        .map(ContatoEmergenciaCreateDTO::toEntity)
                                        .toList()
                        );
                } else {
                        paciente.setContatosEmergencia(new ArrayList<>());
                }

                paciente.setTipoSanguineo(null);
                paciente.setAlergiasConhecidas(new ArrayList<>());
                paciente.setRestricoesAlimentares(new ArrayList<>());
                paciente.setCondicoesClinicas(new ArrayList<>());

                return paciente;
        }
}