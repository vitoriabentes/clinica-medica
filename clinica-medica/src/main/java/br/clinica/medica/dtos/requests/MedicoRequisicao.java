package br.clinica.medica.dtos.requests;

import br.clinica.medica.models.enums.Sexo;
import br.clinica.medica.models.enums.StatusProfissional;

import java.time.LocalDate;
import java.util.Set;

public record MedicoRequisicao(
        String CRM,
        String CPF,
        String nomeCompleto,
        LocalDate dataNascimento,
        Sexo sexo,
        String email,
        String telefone,
        Set<Long> especialidades,
        StatusProfissional status
){}
