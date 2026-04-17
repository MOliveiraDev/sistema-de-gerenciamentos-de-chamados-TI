package ateneu.sgcti.gchamados.dto;

import jakarta.validation.constraints.NotNull;

public record AtribuirTecnicoRequest(
        @NotNull(message = "Técnico é obrigatório")
        Integer tecnicoId
) {
}

