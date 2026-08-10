package com.example.sghss.service;

import com.example.sghss.dto.request.EvolucaoClinicaCreateDTO;
import com.example.sghss.dto.request.PrescricaoMedicaCreateDTO;
import com.example.sghss.dto.response.ProntuarioResponseDTO;
import com.example.sghss.model.*;
import com.example.sghss.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sghss.exception.BusinessException;
import com.example.sghss.model.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ProfissionalSaudeRepository profissionalSaudeRepository;
    private final EvolucaoClinicaRepository evolucaoClinicaRepository;
    private final PrescricaoMedicaRepository prescricaoMedicaRepository;

    @Transactional(readOnly = true)
    public ProntuarioResponseDTO buscarResumoCompletoPorPacienteId(UUID pacienteId) {
        Prontuario prontuario = prontuarioRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Prontuário não encontrado. O cadastro do paciente pode estar corrompido ou o ID é inválido."
                ));

        return ProntuarioResponseDTO.fromEntity(prontuario);
    }

    @Transactional
    public void registrarEvolucao(EvolucaoClinicaCreateDTO dto, Usuario usuarioLogado) {
        Prontuario prontuario = prontuarioRepository.findById(dto.prontuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        ProfissionalSaude autor = profissionalSaudeRepository.findByPessoaFisicaId(usuarioLogado.getPessoaFisica().getId())
                .orElseThrow(() -> new BusinessException("O usuário logado não possui um registro ativo de Profissional de Saúde."));

        EvolucaoClinica evolucao = new EvolucaoClinica();
        evolucao.setProntuario(prontuario);
        evolucao.setAutor(autor);
        evolucao.setEstadoClinico(dto.estadoClinico());
        evolucao.setDescricaoEvolucao(dto.descricaoEvolucao());
        evolucao.setCondutaAdotada(dto.condutaAdotada());

        evolucaoClinicaRepository.save(evolucao);
    }

    @Transactional
    public void registrarPrescricao(PrescricaoMedicaCreateDTO dto, Usuario usuarioLogado) {
        Prontuario prontuario = prontuarioRepository.findById(dto.prontuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));


        ProfissionalSaude autor = profissionalSaudeRepository.findByPessoaFisicaId(usuarioLogado.getPessoaFisica().getId())
                .orElseThrow(() -> new BusinessException("O usuário logado não possui um registro ativo de Profissional de Saúde."));

        PrescricaoMedica prescricao = new PrescricaoMedica();

        String anoPrescricao = String.valueOf(java.time.LocalDate.now().getYear());
        String codigoUnicoPrescricao = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        prescricao.setNumeroPrescricao("PRES-" + anoPrescricao + "-" + codigoUnicoPrescricao);

        prescricao.setProntuario(prontuario);
        prescricao.setProfissionalSaude(autor); // <-- Assinatura Inviolável!
        prescricao.setDataHoraValidade(LocalDateTime.now().plusDays(30));
        prescricao.setObservacoesClinicas(dto.observacoesClinicas());

        var itens = dto.itens().stream().map(itemDto -> {
            ItemPrescricao item = new ItemPrescricao();
            item.setPrescricao(prescricao);
            item.setTipoItem(itemDto.tipoItem());
            item.setDescricaoItem(itemDto.descricaoItem());
            item.setDosagem(itemDto.dosagem());
            item.setViaAdministracao(itemDto.viaAdministracao());
            item.setFrequenciaHoraria(itemDto.frequenciaHoraria());
            item.setInstrucoesDiluicaoAplicacao(itemDto.instrucoesDiluicaoAplicacao());
            return item;
        }).collect(Collectors.toList());

        prescricao.setItens(itens);
        prescricaoMedicaRepository.save(prescricao);
    }
}