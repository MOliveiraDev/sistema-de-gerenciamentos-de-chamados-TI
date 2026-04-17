package ateneu.sgcti.gchamados.relatorios.dto;

import java.time.LocalDate;

public record RelatorioPorPeriodoResponse(
        LocalDate dataInicio,
        LocalDate dataFim,
        Long totalChamados
) {
}


