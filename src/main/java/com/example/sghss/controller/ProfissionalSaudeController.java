package com.example.sghss.controller;

import com.example.sghss.dto.response.ProfissionalResumoDTO;
import com.example.sghss.service.ProfissionalSaudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profissionais")
@RequiredArgsConstructor
public class ProfissionalSaudeController {

    private final ProfissionalSaudeService profissionalSaudeService;

    @GetMapping
    // Liberado para recepcionistas, admins e para os próprios pacientes no futuro aplicativo
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<List<ProfissionalResumoDTO>> listarProfissionaisAtivos() {
        return ResponseEntity.ok(profissionalSaudeService.listarProfissionaisAtivos());
    }

}