package ateneu.sgcti.shared.security;

import ateneu.sgcti.auth.entity.UsuarioEntity;
import ateneu.sgcti.auth.enums.Role;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal implements UserDetails {

    private final transient UsuarioEntity usuario;

    public UsuarioPrincipal(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public Integer getId() {
        return usuario.getId();
    }

    public Role getRole() {
        return usuario.getRole();
    }

    public String getNome() {
        return usuario.getNome();
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public @NonNull String getUsername() {
        return usuario.getEmail();
    }

}


