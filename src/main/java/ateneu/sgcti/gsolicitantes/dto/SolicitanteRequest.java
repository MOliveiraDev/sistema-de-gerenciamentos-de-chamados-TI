package ateneu.sgcti.gsolicitantes.dto;

public record SolicitanteRequest(
        String nome,
        String email,
        String senha,
        String setor,
        String telefone
) {
}

