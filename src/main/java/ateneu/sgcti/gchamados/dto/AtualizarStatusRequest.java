package ateneu.sgcti.gchamados.dto;

import ateneu.sgcti.gchamados.enums.StatusChamado;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull(message = "Status é obrigatório")
        StatusChamado status
) {
}

