package com.example.sghss.dto.response;

import com.example.sghss.model.ProfissionalSaude;

import java.util.List;
import java.util.UUID;

public record ProfissionalResumoDTO(
        UUID id,
        String nome,
        String registroConselho,
        List<EspecialidadeResumoDTO> especialidades // <-- AGORA É UMA LISTA DE OBJETOS COM ID E NOME
) {
    public static ProfissionalResumoDTO fromEntity(ProfissionalSaude profissional) {
        String conselho = profissional.getTipoConselho() + " " + profissional.getNumeroConselho() + "-" + profissional.getUfConselho();

        // Mapeia para o novo DTO
        List<EspecialidadeResumoDTO> listaEspecialidades = profissional.getEspecialidades().stream()
                .map(EspecialidadeResumoDTO::fromEntity)
                .toList();

        return new ProfissionalResumoDTO(
                profissional.getId(),
                profissional.getPessoaFisica().getNome(),
                conselho,
                listaEspecialidades
        );
    }
}