package com.example.sghss.dto.request;

import com.example.sghss.model.enums.TipoItemPrescricao;
import com.example.sghss.model.enums.ViaAdministracao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemPrescricaoCreateDTO(
        @NotNull(message = "O tipo do item é obrigatório.")
        TipoItemPrescricao tipoItem,

        @NotBlank(message = "A descrição do item (remédio/cuidado) é obrigatória.")
        String descricaoItem,

        String dosagem,
        ViaAdministracao viaAdministracao,

        @NotBlank(message = "A frequência/posologia é obrigatória.")
        String frequenciaHoraria,

        String instrucoesDiluicaoAplicacao
) {}