package ateneu.sgcti.auth.service;

import ateneu.sgcti.auth.dto.AuthResponse;
import ateneu.sgcti.auth.dto.LoginRequest;
import ateneu.sgcti.auth.dto.UsuarioAutenticadoResponse;
import ateneu.sgcti.auth.exception.AuthUnauthorizedException;
import ateneu.sgcti.shared.security.JwtService;
import ateneu.sgcti.shared.security.TokenBlacklistService;
import ateneu.sgcti.shared.security.UsuarioPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha())
            );

            UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
            String token = jwtService.generateToken(principal);
            assert principal != null;
            return new AuthResponse(
                    principal.getId(),
                    principal.getNome(),
                    principal.getUsername(),
                    principal.getRole(),
                    "Bearer",
                    token,
                    "Login realizado com sucesso."
            );
        } catch (Exception ex) {
            throw new AuthUnauthorizedException("Credenciais inválidas.");
        }
    }

    public void logout(String token) {
        tokenBlacklistService.revoke(token);
        SecurityContextHolder.clearContext();
    }

    public UsuarioAutenticadoResponse me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UsuarioPrincipal principal)) {
            throw new AuthUnauthorizedException("Usuário não autenticado.");
        }

        return new UsuarioAutenticadoResponse(
                principal.getNome(),
                principal.getUsername(),
                principal.getRole()
        );
    }

}

