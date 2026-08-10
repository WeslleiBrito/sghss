package com.example.sghss.controller;

import com.example.sghss.dto.request.EvolucaoClinicaCreateDTO;
import com.example.sghss.dto.request.PrescricaoMedicaCreateDTO;
import com.example.sghss.dto.response.ProntuarioResponseDTO;
import com.example.sghss.model.Prontuario;
import com.example.sghss.service.ProntuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.sghss.model.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prontuarios")
@RequiredArgsConstructor
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    // 1. O médico clica no paciente da fila e o sistema abre o Prontuário
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'ADMIN')")
    public ResponseEntity<ProntuarioResponseDTO> acessarProntuario(@PathVariable UUID pacienteId) {

        ProntuarioResponseDTO prontuarioCompleto = prontuarioService.buscarResumoCompletoPorPacienteId(pacienteId);
        return ResponseEntity.ok(prontuarioCompleto);

    }

    // 2. O médico anota as queixas (Evolução)
    @PostMapping("/evolucoes")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public ResponseEntity<Void> registrarEvolucao(
            @Valid @RequestBody EvolucaoClinicaCreateDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        prontuarioService.registrarEvolucao(dto, usuarioLogado);
        return ResponseEntity.status(201).build();
    }

    // 3. O médico assina a Receita (Prescrição)
    @PostMapping("/prescricoes")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public ResponseEntity<Void> registrarPrescricao(
            @Valid @RequestBody PrescricaoMedicaCreateDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        prontuarioService.registrarPrescricao(dto, usuarioLogado);
        return ResponseEntity.status(201).build();
    }
}