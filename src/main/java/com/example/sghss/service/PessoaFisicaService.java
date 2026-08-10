package com.example.sghss.service;

import com.example.sghss.dto.request.PessoaFisicaCreateDTO;
import com.example.sghss.model.PessoaFisica;
import com.example.sghss.repository.PessoaFisicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PessoaFisicaService {

    private final PessoaFisicaRepository pessoaFisicaRepository;

    @Transactional
    public PessoaFisica criarOuRecuperarPessoaFisica(PessoaFisicaCreateDTO dto) {
        return pessoaFisicaRepository.findByCpf(dto.cpf())
                .orElseGet(() -> {
            PessoaFisica novaPessoa = dto.toEntity();
            return pessoaFisicaRepository.save(novaPessoa);
        });
    }

}