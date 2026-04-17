package ateneu.sgcti.auth.dto;

import ateneu.sgcti.auth.enums.Role;

public record UsuarioAutenticadoResponse(
        String nome,
        String email,
        Role role
) {
}

