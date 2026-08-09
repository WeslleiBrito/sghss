package com.example.sghss.service;

import com.example.sghss.model.PessoaFisica;
import com.example.sghss.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        // Injeta a chave secreta manualmente já que não estamos subindo o contexto inteiro do Spring
        ReflectionTestUtils.setField(tokenService, "secret", "chave-teste-123");

        usuarioMock = new Usuario();
        usuarioMock.setLogin("admin@sghss.com");
        PessoaFisica pf = new PessoaFisica();
        pf.setNome("Admin Teste");
        usuarioMock.setPessoaFisica(pf);
    }

    @Test
    @DisplayName("Deve gerar um token válido e extrair o login corretamente")
    void deveGerarEValidarTokenComSucesso() {
        // Ação: Gera o token
        String token = tokenService.gerarToken(usuarioMock);

        // Verificações
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Ação: Valida e extrai o login
        String subjectExtraido = tokenService.validarToken(token);
        assertEquals("admin@sghss.com", subjectExtraido);
    }

    @Test
    @DisplayName("Deve retornar string vazia ao tentar validar um token fraudado ou inválido")
    void deveFalharAoValidarTokenInvalido() {
        String tokenFraudado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fraudado";

        String subjectExtraido = tokenService.validarToken(tokenFraudado);

        assertEquals("", subjectExtraido); // A regra de negócio do seu TokenService retorna vazio em caso de erro
    }
}