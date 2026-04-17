package ateneu.sgcti.shared.security;

import ateneu.sgcti.auth.repository.UsuarioRepository;
import ateneu.sgcti.auth.exception.AuthUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .map(UsuarioPrincipal::new)
                .orElseThrow(() -> new AuthUnauthorizedException("Credenciais inválidas."));
    }
}


