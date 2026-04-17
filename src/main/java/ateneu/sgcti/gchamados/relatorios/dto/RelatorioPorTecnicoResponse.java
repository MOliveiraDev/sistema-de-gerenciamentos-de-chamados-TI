package ateneu.sgcti.gchamados.relatorios.dto;

public record RelatorioPorTecnicoResponse(
        Integer tecnicoId,
        String nomeTecnico,
        Long totalChamados
) {
}


