package br.clinica.medica.dtos.responses;

import br.clinica.medica.models.enums.Sexo;

import java.time.LocalDate;

public record PacienteResposta(
        String CPF,
        String nomeCompleto,
        LocalDate dataNascimento,
        Sexo sexo,
        String email,
        String telefone
){}
