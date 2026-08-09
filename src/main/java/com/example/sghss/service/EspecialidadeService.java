package com.example.sghss.service;

import com.example.sghss.dto.response.EspecialidadeResumoDTO;
import com.example.sghss.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    @Transactional(readOnly = true)
    public List<EspecialidadeResumoDTO> listarTodas() {
        return especialidadeRepository.findAll().stream()
                .map(EspecialidadeResumoDTO::fromEntity)
                .toList();
    }
}