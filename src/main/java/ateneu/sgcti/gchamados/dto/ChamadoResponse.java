package ateneu.sgcti.gchamados.dto;

import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import ateneu.sgcti.gchamados.enums.StatusChamado;

import java.time.LocalDateTime;

public record ChamadoResponse(
        Integer id,
        String titulo,
        String descricao,
        PrioridadeChamado prioridade,
        StatusChamado status,
        LocalDateTime dataAbertura,
        LocalDateTime dataAtualizacao,
        LocalDateTime dataFechamento,
        Integer solicitanteId,
        String nomeSolicitante,
        Integer tecnicoId,
        String nomeTecnico
) {
}

