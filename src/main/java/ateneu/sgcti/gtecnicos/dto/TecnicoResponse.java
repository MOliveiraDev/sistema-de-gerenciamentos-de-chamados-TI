package ateneu.sgcti.gtecnicos.dto;

public record TecnicoResponse(
        Integer id,
        Integer usuarioId,
        String nome,
        String email,
        String cpf,
        String especialidade,
        String telefone
) {
}

