package com.example.sghss.repository;
import com.example.sghss.model.EvolucaoClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EvolucaoClinicaRepository extends JpaRepository<EvolucaoClinica, UUID> {}