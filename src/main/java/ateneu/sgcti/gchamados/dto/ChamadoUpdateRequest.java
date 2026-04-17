package ateneu.sgcti.gchamados.dto;

import ateneu.sgcti.gchamados.enums.PrioridadeChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChamadoUpdateRequest(
        @NotBlank(message = "Título é obrigatório")
        @Size(min = 3, max = 45, message = "Título deve ter entre 3 e 45 caracteres")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 3, max = 255, message = "Descrição deve ter entre 3 e 255 caracteres")
        String descricao,

        @NotNull(message = "Prioridade é obrigatória")
        PrioridadeChamado prioridade
) {
}

