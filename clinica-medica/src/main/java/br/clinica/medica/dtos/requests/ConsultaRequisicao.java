package br.clinica.medica.dtos.requests;

import br.clinica.medica.models.enums.StatusConsulta;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ConsultaRequisicao(
        @NotNull(message = "Data e hora são obrigatórias")
        @Future(message = "Data e hora devem ser futuras")
        LocalDateTime dataHoraConsultaInicio,
        StatusConsulta statusConsulta,
        @NotNull(message = "Médico é obrigatório")
        Long medicoId,
        @NotNull(message = "Paciente é obrigatório")
        Long pacienteId,
        @NotNull(message = "Especialidade é obrigatória")
        Long especialidadeId
) {}
