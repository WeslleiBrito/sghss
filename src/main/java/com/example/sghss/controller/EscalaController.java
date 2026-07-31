package com.example.sghss.controller;

import com.example.sghss.dto.response.EscalaResponseDTO;
import com.example.sghss.service.EscalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}