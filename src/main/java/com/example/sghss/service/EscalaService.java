package com.example.sghss.service;


import com.example.sghss.dto.response.EscalaResumoDTO;
import com.example.sghss.model.Escala;
import com.example.sghss.repository.EscalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sghss.dto.response.EscalaResponseDTO;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EscalaService {

    private final EscalaRepository escalaRepository;

    @Transactional
    public Escala validarESalvar(Escala novaEscala) {

        // 1. Validação básica de coerência temporal
        if (novaEscala.getDataHoraInicio().isAfter(novaEscala.getDataHoraFim()) ||
                novaEscala.getDataHoraInicio().isEqual(novaEscala.getDataHoraFim())) {
            throw new IllegalArgumentException("A data/hora de início deve ser anterior à data/hora de fim.");
        }

        // 2. Validação contra o passado (Não faz sentido escalar pro passado)
        if (novaEscala.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível criar uma escala com data no passado.");
        }

        // 3. O CORAÇÃO DO SISTEMA: Verificação de Colisão Global
        boolean estaOcupado = escalaRepository.existeColisaoDeHorario(
                novaEscala.getColaborador().getId(),
                novaEscala.getDataHoraInicio(),
                novaEscala.getDataHoraFim()
        );

        if (estaOcupado) {
            throw new IllegalStateException("Conflito de agenda: O profissional já possui um registro de escala (plantão, ambulatório, folga) que colide com este horário, independentemente da unidade.");
        }

        // Se passou por todas as barreiras, salva no banco!
        return escalaRepository.save(novaEscala);
    }

    @Transactional(readOnly = true)
    public List<EscalaResponseDTO> listarEscalasVigentes() {
        // Busca todas as escalas e filtra as de hoje em diante
        return escalaRepository.findAll().stream()
                .filter(e -> e.getDataHoraFim().isAfter(LocalDateTime.now().minusDays(1)))
                .map(EscalaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EscalaResumoDTO> listarEscalasFuturasPorProfissional(UUID profissionalId) {
        // Pega o momento exato em que a recepcionista clicou no médico
        LocalDateTime agora = LocalDateTime.now();

        return escalaRepository.findByColaboradorIdAndDataHoraInicioAfterOrderByDataHoraInicioAsc(profissionalId, agora)
                .stream()
                .map(EscalaResumoDTO::fromEntity)
                .toList();
    }

}
