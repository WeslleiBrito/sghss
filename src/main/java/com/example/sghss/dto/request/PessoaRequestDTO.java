package com.example.sghss.dto.request;


import java.time.LocalDate;

public record PessoaRequestDTO(
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String email,
        String telefone
) {}
