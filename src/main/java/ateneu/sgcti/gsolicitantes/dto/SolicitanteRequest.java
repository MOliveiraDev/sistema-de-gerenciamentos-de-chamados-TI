package ateneu.sgcti.gsolicitantes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolicitanteRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 45, message = "Nome deve ter entre 3 e 45 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 3, max = 45, message = "Senha deve ter entre 3 e 45 caracteres")
        String senha,

        @NotBlank(message = "Setor é obrigatório")
        @Size(min = 1, max = 45, message = "Setor deve ter no máximo 45 caracteres")
        String setor,

        @NotBlank(message = "Telefone é obrigatório")
        @Size(min = 1, max = 45, message = "Telefone deve ter no máximo 45 caracteres")
        String telefone
) {
}

