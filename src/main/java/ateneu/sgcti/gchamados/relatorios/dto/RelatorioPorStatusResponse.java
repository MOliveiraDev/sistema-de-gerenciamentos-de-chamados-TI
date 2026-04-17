package ateneu.sgcti.gchamados.relatorios.dto;

import ateneu.sgcti.gchamados.enums.StatusChamado;

public record RelatorioPorStatusResponse(
        StatusChamado status,
        Long totalChamados
) {
}


