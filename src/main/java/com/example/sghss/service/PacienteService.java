package com.example.sghss.service;

import com.example.sghss.dto.request.PacienteCreateDTO;
import com.example.sghss.dto.response.PacienteResponseDTO;
import com.example.sghss.model.Paciente;
import com.example.sghss.model.PessoaFisica;
import com.example.sghss.model.Prontuario;
import com.example.sghss.repository.PacienteRepository;
import com.example.sghss.repository.ProntuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PessoaFisicaService pessoaFisicaService;

    // A MÁGICA: Injetamos o repositório do prontuário aqui!
    private final ProntuarioRepository prontuarioRepository;

    @Transactional
    public PacienteResponseDTO cadastrarPaciente(PacienteCreateDTO dto) {
        if (pacienteRepository.existsByCartaoSus(dto.cartaoSus())) {
            throw new IllegalArgumentException("Já existe um paciente cadastrado com este Cartão SUS.");
        }

        PessoaFisica pessoaFisica = pessoaFisicaService.criarOuRecuperarPessoaFisica(dto.pessoaFisica());
        Paciente paciente = dto.toEntity(pessoaFisica);

        // 1. Salva o Paciente primeiro para gerar o UUID dele no banco
        Paciente salvo = pacienteRepository.save(paciente);

        // 2. Cria a "Pasta Virtual" (Prontuário) amarrada ao paciente recém-criado
        Prontuario prontuario = new Prontuario();
        // GERA UM CÓDIGO SEGURO E BONITO: Ex: "PEP-2026-A4B9F2E1"
        String anoAtual = String.valueOf(java.time.LocalDate.now().getYear());
        String codigoUnico = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        prontuario.setNumeroProntuario("PEP-" + anoAtual + "-" + codigoUnico);
        prontuario.setPaciente(salvo);
        prontuarioRepository.save(prontuario);

        // Retorno limpo acionando o construtor do seu Record!
        return new PacienteResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(UUID id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID informado."));
        return new PacienteResponseDTO(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorCpf(String cpf) {
        Paciente paciente = pacienteRepository.findByPessoaFisicaCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado para o CPF informado."));
        return new PacienteResponseDTO(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorCartaoSus(String cartaoSus) {
        Paciente paciente = pacienteRepository.findByCartaoSus(cartaoSus)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado para o Cartão SUS informado."));
        return new PacienteResponseDTO(paciente);
    }
}