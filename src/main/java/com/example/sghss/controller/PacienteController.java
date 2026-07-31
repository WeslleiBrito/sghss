package com.example.sghss.controller;

import com.example.sghss.dto.request.PacienteCreateDTO;
import com.example.sghss.dto.response.PacienteResponseDTO;
import com.example.sghss.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    // 1. ADMISSÃO: Apenas Recepção e Administração podem cadastrar novos pacientes
    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN')")
    public ResponseEntity<PacienteResponseDTO> cadastrar(@Valid @RequestBody PacienteCreateDTO dto) {
        PacienteResponseDTO response = pacienteService.cadastrarPaciente(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    // 2. BUSCA POR ID: Acessível pelo corpo clínico e operacional
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'MEDICO')")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    // 3. BUSCA POR CPF: O endpoint principal para o check-in rápido no balcão da recepção
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'MEDICO')")
    public ResponseEntity<PacienteResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(pacienteService.buscarPorCpf(cpf));
    }

    // 4. BUSCA POR CARTÃO SUS: Crucial para regulação e faturamento
    @GetMapping("/cartao-sus/{cartaoSus}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'MEDICO')")
    public ResponseEntity<PacienteResponseDTO> buscarPorCartaoSus(@PathVariable String cartaoSus) {
        return ResponseEntity.ok(pacienteService.buscarPorCartaoSus(cartaoSus));
    }
}