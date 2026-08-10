package com.example.sghss.controller;

import com.example.sghss.dto.request.AgendamentoCreateDTO;
import com.example.sghss.dto.response.AgendamentoResponseDTO;
import com.example.sghss.dto.response.HorarioDisponivelDTO;
import com.example.sghss.model.Usuario;
import com.example.sghss.model.enums.StatusAgendamento;
import com.example.sghss.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<AgendamentoResponseDTO> agendar(@Valid @RequestBody AgendamentoCreateDTO dto) {
        AgendamentoResponseDTO response = agendamentoService.agendar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN')")
    public ResponseEntity<AgendamentoResponseDTO> realizarCheckIn(@PathVariable UUID id) {
        return ResponseEntity.ok(agendamentoService.realizarCheckIn(id));
    }

    @PatchMapping("/{id}/iniciar")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'ADMIN')")
    public ResponseEntity<AgendamentoResponseDTO> iniciarAtendimento(@PathVariable UUID id) {
        return ResponseEntity.ok(agendamentoService.iniciarAtendimento(id));
    }

    @PatchMapping("/{id}/concluir")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'ADMIN')")
    public ResponseEntity<AgendamentoResponseDTO> concluirAtendimento(@PathVariable UUID id) {
        return ResponseEntity.ok(agendamentoService.concluirAtendimento(id));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE', 'MEDICO', 'ENFERMEIRO')")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(
            @PathVariable UUID id,
            @RequestParam String motivo) {
        return ResponseEntity.ok(agendamentoService.cancelar(id, motivo));
    }

    @GetMapping("/disponiveis")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE', 'MEDICO')")
    public ResponseEntity<List<HorarioDisponivelDTO>> listarHorariosDisponiveis(@RequestParam UUID escalaId) {
        return ResponseEntity.ok(agendamentoService.listarHorariosDisponiveis(escalaId));
    }

    @GetMapping("/hoje")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'MEDICO', 'ENFERMEIRO')")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentosDoDia() {
        return ResponseEntity.ok(agendamentoService.listarAgendamentosDoDia());
    }

    @GetMapping("/medico/{medicoId}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'MEDICO')")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorMedico(@PathVariable UUID medicoId) {
        return ResponseEntity.ok(agendamentoService.listarPorMedico(medicoId));
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN', 'PACIENTE', 'MEDICO')")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorPaciente(@PathVariable UUID pacienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/por-data")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        return ResponseEntity.ok(agendamentoService.buscarAgendaPorData(data));
    }

    @GetMapping("/minha-fila")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarMinhaFila(
            @AuthenticationPrincipal Usuario usuarioLogado) {

        return ResponseEntity.ok(agendamentoService.listarMinhaFila(usuarioLogado));
    }

    @GetMapping("/status")
    public ResponseEntity<List<String>> listarStatusPermitidos() {
        List<String> status = Arrays.stream(StatusAgendamento.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(status);
    }
}
