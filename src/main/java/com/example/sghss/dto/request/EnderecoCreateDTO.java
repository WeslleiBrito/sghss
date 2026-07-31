package com.example.sghss.dto.request;

import com.example.sghss.model.valueobject.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoCreateDTO(
        @NotBlank(message = "O CEP é obrigatório.")
        @Pattern(regexp = "\\d{8}|\\d{5}-\\d{3}", message = "O CEP deve ser enviado no formato 00000000 ou 00000-000.")
        String cep,

        @NotBlank(message = "O logradouro (rua, avenida, etc.) é obrigatório.")
        String logradouro,

        @NotBlank(message = "O número é obrigatório. Caso não possua, informe 'S/N'.")
        String numero,

        String complemento,

        @NotBlank(message = "O bairro é obrigatório.")
        String bairro,

        @NotBlank(message = "A cidade é obrigatória.")
        String cidade,

        @NotBlank(message = "A UF (Estado) é obrigatória.")
        @Size(min = 2, max = 2, message = "A UF deve conter exatamente 2 caracteres (Ex: BA, SP, RJ).")
        String uf
) {
    // Conversão limpa para o Value Object Endereco [source: 8]
    public Endereco toEntity() {
        Endereco endereco = new Endereco();
        endereco.setCep(this.cep());
        endereco.setLogradouro(this.logradouro());
        endereco.setNumero(this.numero());
        endereco.setComplemento(this.complemento());
        endereco.setBairro(this.bairro());
        endereco.setCidade(this.cidade());
        endereco.setUf(this.uf());
        return endereco;
    }
}