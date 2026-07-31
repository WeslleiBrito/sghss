package com.example.sghss.repository;

import com.example.sghss.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstituicaoRepository extends JpaRepository<Instituicao, UUID> {
}
