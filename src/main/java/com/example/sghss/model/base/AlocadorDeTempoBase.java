package com.example.sghss.model.base;


import com.example.sghss.model.Escala;
import com.example.sghss.service.EscalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AlocadorDeTempoBase {

    // O Cérebro Central injetado no pai
    private final EscalaService escalaService;

    // 1. A FUNÇÃO ABSTRATA: Toda unidade filha é OBRIGADA a implementar isso!
    // Se o programador não implementar, o código nem compila.
    protected abstract Escala montarRegraEspecifica(Object dadosDaUnidade);

    // 2. O FLUXO FECHADO (Template Method): Nenhuma classe filha pode alterar esse fluxo.
    @Transactional
    public final Escala executarAlocacao(Object dadosDaUnidade) {

        // Passo 1: Chama a regra da filha (O hospital monta o plantão, a clínica monta a agenda)
        Escala escalaMontada = montarRegraEspecifica(dadosDaUnidade);

        // Passo 2: O pai força a validação e o salvamento no Cérebro Central!
        // A classe filha não consegue fugir disso.
        return escalaService.validarESalvar(escalaMontada);
    }
}