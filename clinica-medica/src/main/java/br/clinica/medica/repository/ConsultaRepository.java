package br.clinica.medica.repository;

import br.clinica.medica.exceptions.ResourceNotFoundException;
import br.clinica.medica.models.Consulta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.models.enums.StatusConsulta;
import br.clinica.medica.rowMapper.ConsultaRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ConsultaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConsultaRowMapper consultaRowMapper;

    public Consulta agendarConsulta(Consulta consulta){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = """
            INSERT INTO CONSULTAS (DATA_HORA_INICIO, DATA_HORA_TERMINO,
                                   STATUS, MEDICO_ID, PACIENTE_ID, ESPECIALIDADE_ID)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(consulta.getDataHoraConsultaInicio()));
            ps.setTimestamp(2, Timestamp.valueOf(consulta.getDataHoraConsultaTermino()));
            ps.setString(3, consulta.getStatusConsulta().toString());
            ps.setLong(4, consulta.getMedicoId());
            ps.setLong(5, consulta.getPacienteId());
            ps.setLong(6, consulta.getEspecialidadeId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            Long id = keyHolder.getKey().longValue();
            consulta.setId(id);
        }
        return consulta;
    }

    public void cancelarConsulta(Long id) {
        String query = """ 
                DELETE FROM CONSULTAS WHERE ID = ?
                """;
        jdbcTemplate.update(query, id);
    }

    public List<Consulta> buscarAgendaMedico(Long medicoId) {
        String query = """
                SELECT * FROM CONSULTAS
                WHERE MEDICO_ID = ?
                """;
        return jdbcTemplate.query(query, consultaRowMapper, medicoId);
    }

    public List<Consulta> buscarAgendaPaciente(Long pacienteId) {
        String query = """
                SELECT * FROM CONSULTAS
                WHERE PACIENTE_ID = ?
                """;
        return jdbcTemplate.query(query, consultaRowMapper, pacienteId);
    }

    public Consulta buscarConsultaPorID(Long id) {
        String query = """
                SELECT * FROM CONSULTAS WHERE ID = ?
                """;
        try {
            return jdbcTemplate.queryForObject(query, consultaRowMapper, id);
        }catch (RuntimeException e){
            throw new ResourceNotFoundException("Consulta não cadastrada no sistema.");
        }
    }

    public void confirmarConsulta(Long id) {
        String query = """ 
                   UPDATE CONSULTAS
                   SET STATUS = ?
                   WHERE ID = ?
                """;

        jdbcTemplate.update(query,String.valueOf(StatusConsulta.CONFIRMADO),id);
    }

    public void reagendarConsulta(Consulta consultaAtualizada) {
        String query = """
            UPDATE CONSULTAS
            SET DATA_HORA_INICIO = ?, DATA_HORA_TERMINO = ?, STATUS = ?,
                MEDICO_ID = ?, PACIENTE_ID = ?, ESPECIALIDADE_ID = ?
            WHERE ID = ?
            """;
        jdbcTemplate.update(query,
                Timestamp.valueOf(consultaAtualizada.getDataHoraConsultaInicio()),
                Timestamp.valueOf(consultaAtualizada.getDataHoraConsultaTermino()),
                consultaAtualizada.getStatusConsulta().toString(),
                consultaAtualizada.getMedicoId(),
                consultaAtualizada.getPacienteId(),
                consultaAtualizada.getEspecialidadeId(),
                consultaAtualizada.getId()
        );
    }
}
