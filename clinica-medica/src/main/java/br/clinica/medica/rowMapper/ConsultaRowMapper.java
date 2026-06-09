package br.clinica.medica.rowMapper;

import br.clinica.medica.models.Consulta;
import br.clinica.medica.models.enums.StatusConsulta;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ConsultaRowMapper implements RowMapper<Consulta> {

    @Override
    public Consulta mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Consulta consulta = new Consulta();

        consulta.setId(resultSet.getLong("ID"));
        consulta.setDataHoraConsultaInicio(resultSet.getTimestamp("DATA_HORA_INICIO").toLocalDateTime());
        consulta.setDataHoraConsultaTermino(resultSet.getTimestamp("DATA_HORA_TERMINO").toLocalDateTime());
        consulta.setStatusConsulta(StatusConsulta.valueOf(resultSet.getString("STATUS_CONSULTA")));
        consulta.setMedicoId(resultSet.getLong("MEDICO_ID"));
        consulta.setPacienteId(resultSet.getLong("PACIENTE_ID"));
        consulta.setEspecialidadeId(resultSet.getLong("ESPECIALIDADE_ID"));
        consulta.setCriadoEm(resultSet.getTimestamp("CRIADO_EM").toLocalDateTime());
        consulta.setAtualizadoEm(resultSet.getTimestamp("ATUALIZADO_EM").toLocalDateTime());

        return consulta;
    }
}
