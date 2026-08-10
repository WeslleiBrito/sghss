package com.example.sghss.model;

import com.example.sghss.model.base.EntidadeBase;
import com.example.sghss.model.enums.PerfilAcesso;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
public class Usuario extends EntidadeBase implements UserDetails {


    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_fisica_id", nullable = false, unique = true)
    private PessoaFisica pessoaFisica;

    @Column(nullable = false, unique = true, length = 100)
    private String login;

    @Column(nullable = false)
    private String senha;


    @CollectionTable(name = "tb_usuario_perfil", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private Set<PerfilAcesso> perfisAcesso;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfisAcesso.stream()
                .flatMap(perfil -> perfil.getAuthorities().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() { return this.senha; }
    @Override
    // public String getUsername() { return this.login; }
    public String getUsername() {return this.pessoaFisica.getNome();}
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return this.ativo; }
}