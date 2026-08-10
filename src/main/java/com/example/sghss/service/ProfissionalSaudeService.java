package com.example.sghss.service;

import com.example.sghss.dto.response.ProfissionalResumoDTO;
import com.example.sghss.repository.ProfissionalSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository profissionalSaudeRepository;

    @Transactional(readOnly = true)
    public List<ProfissionalResumoDTO> listarProfissionaisAtivos() {
        return profissionalSaudeRepository.findProfissionaisComEscalaFutura()
                .stream()
                .map(ProfissionalResumoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResumoDTO> listarPorEspecialidade(UUID especialidadeId) {
        return profissionalSaudeRepository.findPorEspecialidadeComEscalaFutura(especialidadeId)
                .stream()
                .map(ProfissionalResumoDTO::fromEntity)
                .toList();
    }
}