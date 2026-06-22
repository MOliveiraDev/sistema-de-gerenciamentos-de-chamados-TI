package ateneu.sgcti.auth.dto;

import ateneu.sgcti.auth.enums.Role;

public record UsuarioAutenticadoResponse(
        Integer usuarioId,
        String nome,
        String email,
        Role role,
        Integer solicitanteId,
        Integer tecnicoId
) {
}
