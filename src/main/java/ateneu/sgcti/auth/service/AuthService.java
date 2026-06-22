package ateneu.sgcti.auth.service;

import ateneu.sgcti.auth.dto.AuthResponse;
import ateneu.sgcti.auth.dto.LoginRequest;
import ateneu.sgcti.auth.dto.UsuarioAutenticadoResponse;
import ateneu.sgcti.auth.enums.Role;
import ateneu.sgcti.auth.exception.AuthUnauthorizedException;
import ateneu.sgcti.gsolicitantes.entity.SolicitanteEntity;
import ateneu.sgcti.gsolicitantes.repository.SolicitanteRepository;
import ateneu.sgcti.gtecnicos.entity.TecnicoEntity;
import ateneu.sgcti.gtecnicos.repository.TecnicoRepository;
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
    private final SolicitanteRepository solicitanteRepository;
    private final TecnicoRepository tecnicoRepository;

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

        Integer solicitanteId = null;
        Integer tecnicoId = null;

        if (principal.getRole() == Role.SOLICITANTE) {
            solicitanteId = solicitanteRepository.findByUsuarioEntity_Id(principal.getId())
                    .map(SolicitanteEntity::getId)
                    .orElse(null);
        } else if (principal.getRole() == Role.TECNICO) {
            tecnicoId = tecnicoRepository.findByUsuarioEntity_Id(principal.getId())
                    .map(TecnicoEntity::getId)
                    .orElse(null);
        }

        return new UsuarioAutenticadoResponse(
                principal.getId(),
                principal.getNome(),
                principal.getUsername(),
                principal.getRole(),
                solicitanteId,
                tecnicoId
        );
    }
}
