package ateneu.sgcti.gtecnicos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TecnicoRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 45, message = "Nome deve ter entre 3 e 45 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 255, message = "Senha deve ter entre 6 e 255 caracteres")
        String senha,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
        String cpf,

        @NotBlank(message = "Especialidade é obrigatória")
        @Size(min = 1, max = 45, message = "Especialidade deve ter no máximo 45 caracteres")
        String especialidade,

        @NotBlank(message = "Telefone é obrigatório")
        @Size(min = 1, max = 45, message = "Telefone deve ter no máximo 45 caracteres")
        String telefone
) {
}

