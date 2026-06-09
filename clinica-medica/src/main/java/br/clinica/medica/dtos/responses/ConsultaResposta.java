package br.clinica.medica.dtos.responses;

import br.clinica.medica.models.enums.StatusConsulta;

import java.time.LocalDateTime;

public record ConsultaResposta(
        Long id,
        LocalDateTime dataHoraConsultaInicio,
        LocalDateTime dataHoraConsultaTermino,
        StatusConsulta statusConsulta,
        MedicoResposta medico,
        PacienteResposta paciente,
        EspecialidadeResposta especialidade
) {}
