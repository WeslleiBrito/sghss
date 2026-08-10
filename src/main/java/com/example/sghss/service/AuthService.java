package com.example.sghss.service;

import com.example.sghss.dto.request.LoginRequestDTO;
import com.example.sghss.dto.response.TokenResponseDTO;
import com.example.sghss.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public TokenResponseDTO autenticar(LoginRequestDTO dto) {

        var credenciais = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());

        var authentication = authenticationManager.authenticate(credenciais);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        assert usuarioLogado != null;
        String tokenJwt = tokenService.gerarToken(usuarioLogado);

        Set<String> perfis = usuarioLogado.getPerfisAcesso().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new TokenResponseDTO(tokenJwt, "Bearer", usuarioLogado.getLogin(), usuarioLogado.getUsername(), perfis);
    }
}