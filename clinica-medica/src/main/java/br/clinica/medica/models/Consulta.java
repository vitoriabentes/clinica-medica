package br.clinica.medica.models;

import br.clinica.medica.models.enums.StatusConsulta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    private Long id;
    private LocalDateTime dataHoraConsultaInicio;
    private LocalDateTime dataHoraConsultaTermino;
    private int duracaoConsulta;
    private StatusConsulta statusConsulta;
    private Long medicoId;
    private Long pacienteId;
    private Long especialidadeId;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Consulta(LocalDateTime dataHoraConsultaInicio, LocalDateTime dataHoraConsultaTermino, StatusConsulta statusConsulta,
                    Long medicoId, Long pacienteId, Long especialidadeId) {
        this.dataHoraConsultaInicio = dataHoraConsultaInicio;
        this.dataHoraConsultaTermino = dataHoraConsultaTermino;
        this.statusConsulta = statusConsulta;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
        this.especialidadeId = especialidadeId;
    }
}
