package com.example.sghss.controller;

import com.example.sghss.dto.response.EspecialidadeResumoDTO;
import com.example.sghss.dto.response.ProfissionalResumoDTO;
import com.example.sghss.service.EspecialidadeService;
import com.example.sghss.service.ProfissionalSaudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;
    private final ProfissionalSaudeService profissionalSaudeService;
    @GetMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<List<EspecialidadeResumoDTO>> listarTodas() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    @GetMapping("/especialidade/{especialidadeId}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<List<ProfissionalResumoDTO>> listarPorEspecialidade(@PathVariable UUID especialidadeId) {
        return ResponseEntity.ok(profissionalSaudeService.listarPorEspecialidade(especialidadeId));
    }
}