package com.example.sghss.dto.response;

import com.example.sghss.model.Paciente;
import com.example.sghss.model.enums.CondicaoClinica;
import com.example.sghss.model.enums.RestricaoAlimentar;
import com.example.sghss.model.enums.TipoSanguineo;
import com.example.sghss.model.valueobject.ContatoEmergencia;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PacienteResponseDTO(
        UUID id,
        PessoaResponseDTO pessoaFisica, // Reaproveitamos o seu DTO perfeitamente!
        String cartaoSus,
                LocalDate dataCadastroClinico,
                Boolean ativo,
                TipoSanguineo tipoSanguineo,
                List<String> alergiasConhecidas,
                List<RestricaoAlimentar> restricoesAlimentares,
                List<CondicaoClinica> condicoesClinicas,
                String anotacoesGerais,
                List<ContatoEmergencia> contatosEmergencia
) {
    // A MÁGICA DO CLEAN CODE: O próprio Record extrai os dados da Entidade!
    public PacienteResponseDTO(Paciente paciente) {
        this(
                paciente.getId(),
                new PessoaResponseDTO(paciente.getPessoaFisica()), // Aciona o construtor do seu DTO civil [source: 11]
                paciente.getCartaoSus(),
        paciente.getDataCadastroClinico(),
        paciente.getAtivo(),
        paciente.getTipoSanguineo(),
        paciente.getAlergiasConhecidas(),
        paciente.getRestricoesAlimentares(),
        paciente.getCondicoesClinicas(),
        paciente.getAnotacoesGerais(),
        paciente.getContatosEmergencia()
        );
    }
}