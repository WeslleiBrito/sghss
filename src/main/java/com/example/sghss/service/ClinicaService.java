package com.example.sghss.service;


import com.example.sghss.model.Clinica;
import com.example.sghss.model.base.Colaborador;
import com.example.sghss.model.Escala;
import com.example.sghss.model.enums.TipoAtividade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClinicaService {

    // A Clínica "usa" o Cérebro Central, mas não herda dele.
    private final EscalaService escalaService;

    // Injetaríamos aqui também o ClinicaRepository, InsumoRepository, etc.

    @Transactional
    public Escala disponibilizarAgendaAmbulatorial(Colaborador medico, Clinica clinica, LocalDateTime inicio, LocalDateTime fim) {

        // A Clínica monta a escala com a SUA regra de negócio (AMBULATÓRIO)
        Escala escala = new Escala();
        escala.setColaborador(medico);
        escala.setUnidadeSaude(clinica);
        escala.setDataHoraInicio(inicio);
        escala.setDataHoraFim(fim);
        escala.setTipoAtividade(TipoAtividade.AMBULATORIO);

        // A Clínica é obrigada a passar pela catraca do Cérebro Central para salvar
        return escalaService.validarESalvar(escala);
    }

    // Outros métodos da Clínica cabem aqui perfeitamente:
    // public void registrarEntradaInsumos(...)
    // public void fecharCaixaDoDia(...)
}