package com.example.sghss.controller;

import com.example.sghss.dto.response.EscalaResponseDTO;
import com.example.sghss.dto.response.EscalaResumoDTO;
import com.example.sghss.service.EscalaService;
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
@RequestMapping("/api/v1/escalas")
@RequiredArgsConstructor
public class EscalaController {

    private final EscalaService escalaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE', 'MEDICO')")
    public ResponseEntity<List<EscalaResponseDTO>> listarEscalas() {
        return ResponseEntity.ok(escalaService.listarEscalasVigentes());
    }

    @GetMapping("/profissional/{profissionalId}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<List<EscalaResumoDTO>> listarEscalasDoProfissional(@PathVariable UUID profissionalId) {
        return ResponseEntity.ok(escalaService.listarEscalasFuturasPorProfissional(profissionalId));
    }
}