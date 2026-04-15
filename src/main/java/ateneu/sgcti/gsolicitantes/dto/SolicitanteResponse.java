package ateneu.sgcti.gsolicitantes.dto;

public record SolicitanteResponse(
        Integer id,
        Integer usuarioId,
        String nome,
        String email,
        String setor,
        String telefone
) {
}

