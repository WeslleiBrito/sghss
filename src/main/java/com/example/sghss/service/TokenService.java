package com.example.sghss.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.sghss.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Lê do application.properties ou usa um valor padrão forte para o MVP
    @Value("${api.security.token.secret:sghss-chave-secreta-mvp-2026}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("SGHSS-API")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT para o usuário: " + usuario.getLogin(), exception);
        }
    }

    public String validarToken(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("SGHSS-API")
                    .build()
                    .verify(tokenJWT)
                    .getSubject(); // Devolve o login (e-mail/CPF) extraído do token
        } catch (JWTVerificationException exception) {
            return ""; // Se expirado ou fraudado, retorna string vazia para o filtro rejeitar
        }
    }

    private Instant gerarDataExpiracao() {
        // Horário de Brasília (-03:00) com validade de 8 horas
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }
}