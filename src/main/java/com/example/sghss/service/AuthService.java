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
        // 1. Cria o objeto de credenciais nativo do Spring Security
        var credenciais = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());

        // 2. Aciona o banco de dados via UserDetailsService. Se a senha errar, lança BadCredentialsException aqui mesmo!
        var authentication = authenticationManager.authenticate(credenciais);

        // 3. Extrai o nosso usuário logado do objeto de autenticação
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // 4. Fabrica o token JWT
        String tokenJwt = tokenService.gerarToken(usuarioLogado);

        // 5. Extrai os perfis como Strings normais (ex: "ROLE_RECEPCIONISTA", "ROLE_MEDICO")
        Set<String> perfis = usuarioLogado.getPerfisAcesso().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new TokenResponseDTO(tokenJwt, "Bearer", usuarioLogado.getLogin(), perfis);
    }
}