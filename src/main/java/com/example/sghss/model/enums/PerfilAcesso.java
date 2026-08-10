package com.example.sghss.model.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum PerfilAcesso {

    // 1. PROFISSIONAL DE SAÚDE (MÉDICO): Poder clínico total, zero poder financeiro ou de sistema
    ROLE_MEDICO(Set.of(
            Permissao.PRONTUARIO_LER,
            Permissao.PRONTUARIO_ESCREVER,
            Permissao.EXAME_SOLICITAR,
            Permissao.EXAME_LAUDAR,
            Permissao.AGENDAMENTO_CRIAR
    )),

    // 2. ENFERMAGEM: Acesso clínico para evolução, anotações de enfermagem e triagem
    ROLE_ENFERMEIRO(Set.of(
            Permissao.PRONTUARIO_LER,
            Permissao.PRONTUARIO_ESCREVER
    )),

    // 3. RECEPCIONISTA: Poder operacional na agenda e check-in
    ROLE_RECEPCIONISTA(Set.of(
            Permissao.AGENDAMENTO_CRIAR,
            Permissao.AGENDAMENTO_CANCELAR,
            Permissao.CHECKIN_REALIZAR,
            Permissao.CAIXA_OPERAR
    )),

    // 4. SEGURANÇA / APOIO: Enxuto, vê apenas o fluxo de portaria
    ROLE_SEGURANCA(Set.of(
            Permissao.FLUXO_PORTARIA_LER
    )),

    // 5. ADMINISTRADOR: Gestão do sistema e RH, mas BLINDADO contra o domínio clínico!
    ROLE_ADMIN(Set.of(
            Permissao.USUARIO_CRIAR,
            Permissao.USUARIO_GERENCIAR,
            Permissao.CONFIGURACAO_SISTEMA,
            Permissao.FATURAMENTO_GERENCIAR,
            Permissao.AGENDAMENTO_CANCELAR
    )),

    // 6. PACIENTE: Acesso restrito apenas para interagir com seus próprios dados via App
    ROLE_PACIENTE(Set.of(
            Permissao.PRONTUARIO_LER,
            Permissao.AGENDAMENTO_CRIAR
    ));

    private final Set<Permissao> permissoes;

    PerfilAcesso(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }


    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissoes()
                .stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getStringPermissao()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(this.name()));
        return authorities;
    }
}