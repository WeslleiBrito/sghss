package com.example.sghss.repository;

import com.example.sghss.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, UUID> {}