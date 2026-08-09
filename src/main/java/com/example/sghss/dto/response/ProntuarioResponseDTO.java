package com.example.sghss.dto.response;

import com.example.sghss.model.Prontuario;
import com.example.sghss.model.enums.CondicaoClinica;
import com.example.sghss.model.enums.RestricaoAlimentar;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

public record ProntuarioResponseDTO(
        UUID prontuarioId,
        String numeroProntuario,
        UUID pacienteId,
        String nomePaciente,
        Integer idade,
        String cartaoSus,
        String tipoSanguineo,
        List<String> alergiasConhecidas,
        List<String> restricoesAlimentares,
        List<String> condicoesClinicas,
        List<EvolucaoClinicaResponseDTO> historicoEvolucoes
) {
    public static ProntuarioResponseDTO fromEntity(Prontuario prontuario) {

        // 1. Calcula a idade exata do paciente baseada na data de nascimento
        LocalDate nascimento = prontuario.getPaciente().getPessoaFisica().getDataNascimento();
        int idadeCalculada = Period.between(nascimento, LocalDate.now()).getYears();

        // 2. Formata os Enums de alertas para textos legíveis
        String sangue = prontuario.getPaciente().getTipoSanguineo() != null
                ? prontuario.getPaciente().getTipoSanguineo().getRotulo()
                : "Não Informado";

        List<String> restricoes = prontuario.getPaciente().getRestricoesAlimentares().stream()
                .map(RestricaoAlimentar::getDescricao).toList();

        List<String> condicoes = prontuario.getPaciente().getCondicoesClinicas().stream()
                .map(CondicaoClinica::getDescricao).toList();

        // 3. Mapeia o histórico de evoluções médicas
        List<EvolucaoClinicaResponseDTO> evolucoes = prontuario.getEvolucoes().stream()
                .map(evo -> new EvolucaoClinicaResponseDTO(
                        evo.getId(),
                        evo.getAutor().getPessoaFisica().getNome(),
                        evo.getAutor().getIdentificacaoProfissional(), // Ex: CRM/BA 12345
                        evo.getDataHoraEvolucao(),
                        evo.getEstadoClinico(),
                        evo.getDescricaoEvolucao(),
                        evo.getCondutaAdotada()
                )).toList();

        return new ProntuarioResponseDTO(
                prontuario.getId(),
                prontuario.getNumeroProntuario(),
                prontuario.getPaciente().getId(),
                prontuario.getPaciente().getPessoaFisica().getNome(),
                idadeCalculada,
                prontuario.getPaciente().getCartaoSus(),
                sangue,
                prontuario.getPaciente().getAlergiasConhecidas(),
                restricoes,
                condicoes,
                evolucoes
        );
    }
}