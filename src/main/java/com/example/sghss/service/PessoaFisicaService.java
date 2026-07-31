package com.example.sghss.service;

import com.example.sghss.dto.request.PessoaFisicaCreateDTO;
import com.example.sghss.model.PessoaFisica;
import com.example.sghss.repository.PessoaFisicaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PessoaFisicaService {

    private final PessoaFisicaRepository pessoaFisicaRepository;

    @Transactional
    public PessoaFisica criarOuRecuperarPessoaFisica(PessoaFisicaCreateDTO dto) {
        // Se a pessoa já existe pelo CPF na base civil da instituição, reaproveitamos o registro!
        return pessoaFisicaRepository.findByCpf(dto.cpf())
                .orElseGet(() -> {
            PessoaFisica novaPessoa = dto.toEntity(); // Apenas 1 linha de mapeamento!
            return pessoaFisicaRepository.save(novaPessoa);
        });
    }

    public PessoaFisica buscarPorId(UUID id) {
        return pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada com o ID informado."));
    }

    public PessoaFisica buscarPorCpf(String cpf) {
        return pessoaFisicaRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa física não encontrada com o CPF informado."));
    }
}