package br.clinica.medica.dtos.responses;

import br.clinica.medica.models.enums.Sexo;
import br.clinica.medica.models.enums.StatusProfissional;

import java.time.LocalDate;
import java.util.Set;

public record MedicoResposta(
        Long  id,
        String CRM,
        String CPF,
        String nomeCompleto,
        LocalDate dataNascimento,
        Sexo sexo,
        String email,
        String telefone,
        StatusProfissional status,
        Set<EspecialidadeResposta> especialidades
){}
