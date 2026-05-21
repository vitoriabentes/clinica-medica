package br.clinica.medica.dtos.requests;

import br.clinica.medica.models.enums.Sexo;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PacienteRequisicao(
        @NotBlank(message = "CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String CPF,

        @NotBlank(message = "Nome do paciente é obrigatório")
        @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
        @Pattern(regexp = "^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ\\s]+$",
                message = "Nome deve conter apenas letras e espaços")
        String nomeCompleto,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dataNascimento,

        @NotNull(message = "Sexo é obrigatório")
        Sexo sexo,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @Pattern(regexp = "\\(\\d{2}\\)\\d{4,5}-\\d{4}",
                message = "Telefone inválido. Formato: (11)99999-8888")
        String telefone
) {}
