package com.example.sghss.repository;
import com.example.sghss.model.ItemPrescricao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ItemPrescricaoRepository extends JpaRepository<ItemPrescricao, UUID> {}