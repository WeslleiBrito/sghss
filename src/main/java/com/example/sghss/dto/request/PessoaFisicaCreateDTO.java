package com.example.sghss.dto.request;

import com.example.sghss.model.PessoaFisica;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

public record PessoaFisicaCreateDTO(
        @NotBlank(message = "O nome completo é obrigatório.")
        String nome,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos, sem pontos ou traços.")
        String cpf,

        @NotNull(message = "A data de nascimento é obrigatória.")
        LocalDate dataNascimento,

        @Valid
        @NotEmpty(message = "A pessoa deve possuir pelo menos um contato registrado.")
        List<ContatoCreateDTO> contatos,

        @Valid
        @NotEmpty(message = "A pessoa deve possuir pelo menos um endereço registrado.")
        List<EnderecoCreateDTO> enderecos
) {
    public PessoaFisica toEntity() {
        PessoaFisica pessoa = new PessoaFisica();
        pessoa.setNome(this.nome());
        pessoa.setCpf(this.cpf());
        pessoa.setDataNascimento(this.dataNascimento());
        pessoa.setContatos(this.contatos().stream().map(ContatoCreateDTO::toEntity).toList());
        pessoa.setEnderecos(this.enderecos().stream().map(EnderecoCreateDTO::toEntity).toList());
        return pessoa;
    }
}