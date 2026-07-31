package com.example.sghss.service;


import com.example.sghss.model.Hospital;
import com.example.sghss.model.base.Colaborador;
import com.example.sghss.model.Escala;
import com.example.sghss.model.enums.TipoAtividade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final EscalaService escalaService;
    // private final LeitoRepository leitoRepository;

    @Transactional
    public Escala escalarParaPlantaoUti(Colaborador medico, Hospital hospital, LocalDateTime inicioPlantao) {

        // O Hospital monta a escala com a SUA regra de negócio (PLANTÃO de 12 horas)
        Escala escala = new Escala();
        escala.setColaborador(medico);
        escala.setUnidadeSaude(hospital);
        escala.setDataHoraInicio(inicioPlantao);
        escala.setDataHoraFim(inicioPlantao.plusHours(12)); // Regra de 12h cravada!
        escala.setTipoAtividade(TipoAtividade.PLANTAO);

        // O Hospital também não escapa da catraca de colisão global
        return escalaService.validarESalvar(escala);
    }

    // Outros métodos do Hospital:
    // public void transferirPacienteDeLeito(...)
}