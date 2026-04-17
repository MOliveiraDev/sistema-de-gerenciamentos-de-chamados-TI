package ateneu.sgcti.auth.dto;

import ateneu.sgcti.auth.enums.Role;

public record AuthResponse(
        Integer usuarioId,
        String nome,
        String email,
        Role role,
        String tokenType,
        String accessToken,
        String mensagem
) {
}

