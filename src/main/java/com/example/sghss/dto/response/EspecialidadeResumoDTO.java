package com.example.sghss.dto.response;
import com.example.sghss.model.Especialidade;
import java.util.UUID;

public record EspecialidadeResumoDTO(UUID id, String nome) {
    public static EspecialidadeResumoDTO fromEntity(Especialidade e) {
        return new EspecialidadeResumoDTO(e.getId(), e.getNome());
    }
}