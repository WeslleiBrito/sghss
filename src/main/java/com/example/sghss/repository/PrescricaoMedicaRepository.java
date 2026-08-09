package com.example.sghss.repository;
import com.example.sghss.model.PrescricaoMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PrescricaoMedicaRepository extends JpaRepository<PrescricaoMedica, UUID> {}