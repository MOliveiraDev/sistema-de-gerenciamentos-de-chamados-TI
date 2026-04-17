package ateneu.sgcti.gchamados.dto;

import ateneu.sgcti.gchamados.enums.StatusChamado;

import java.time.LocalDateTime;

public record ChamadoHistoricoResponse(
        Integer id,
        LocalDateTime dataEvento,
        String descricaoEvento,
        StatusChamado statusAnterior,
        StatusChamado statusNovo,
        Integer tecnicoAnteriorId,
        Integer tecnicoNovoId
) {
}

