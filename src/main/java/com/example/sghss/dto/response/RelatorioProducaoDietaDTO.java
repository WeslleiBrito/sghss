package com.example.sghss.dto.response;


import com.example.sghss.model.enums.RestricaoAlimentar;

public record RelatorioProducaoDietaDTO(
        RestricaoAlimentar tipoDieta,
        Long quantidade
) {
    // Método utilitário opcional: devolve o texto limpo para a tela da cozinha!
    // Ao serializar para JSON, o Spring/Jackson cria automaticamente um campo "descricaoDieta"
    public String getDescricaoDieta() {
        return tipoDieta != null ? tipoDieta.getDescricao() : "Dieta Geral (Sem Restrições)";
    }
}