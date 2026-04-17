package ateneu.sgcti.gchamados.relatorios.dto;

import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.enums.StatusChamado;

import java.time.LocalDate;

public record RelatorioPdfFiltro(
        StatusChamado status,
        PrioridadeChamado prioridade,
        Integer tecnicoId,
        Integer solicitanteId,
        LocalDate inicio,
        LocalDate fim
) {
}

