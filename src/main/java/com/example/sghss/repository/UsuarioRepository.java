package com.example.sghss.repository;

import com.example.sghss.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Essencial para o TokenService e para o SecurityFilter encontrarem quem está logando
    Optional<Usuario> findByLogin(String login);

    // Essencial para o nosso Seeder saber se precisa criar o ator de teste ou não
    boolean existsByLogin(String login);
}