package br.clinica.medica.repository;

import br.clinica.medica.exceptions.ResourceNotFoundException;
import br.clinica.medica.models.Paciente;
import br.clinica.medica.rowMapper.PacienteRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class PacienteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PacienteRowMapper pacienteRowMapper;

    public Paciente buscarPorId(Long id) {
        String query = """
                SELECT * FROM PACIENTES WHERE ID = ?
                """;

        try {
            return jdbcTemplate.queryForObject(query, pacienteRowMapper, id);
        }catch (RuntimeException e){
            throw new ResourceNotFoundException("Paciente não cadastrado no sistema.");
        }
    }

    public Optional<Paciente> buscarPacientePorCPF(String CPF){
        String query = """
                SELECT * FROM PACIENTES WHERE CPF = ?
                """;

        try {
            Paciente paciente = jdbcTemplate.queryForObject(query, pacienteRowMapper, CPF);
            return Optional.ofNullable(paciente);

        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Optional<Paciente> buscarPacientePorEmail(String email){
        String query = """
                SELECT * FROM PACIENTES WHERE EMAIL = ?
                """;

        try {
            Paciente paciente = jdbcTemplate.queryForObject(query, pacienteRowMapper, email);
            return Optional.ofNullable(paciente);

        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Paciente cadastrarPaciente(Paciente paciente){
        String query = """
                INSERT INTO PACIENTES (NOME_COMPLETO, CPF, EMAIL, TELEFONE, DATA_NASCIMENTO, SEXO)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, paciente.getNomeCompleto());
            ps.setString(2, paciente.getCPF());
            ps.setString(3, paciente.getEmail());
            ps.setString(4, paciente.getTelefone());
            ps.setDate(5, Date.valueOf(paciente.getDataNascimento()));
            ps.setString(6, String.valueOf(paciente.getSexo()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            Long id = keyHolder.getKey().longValue();
            paciente.setId(id);
        }
        return paciente;
    }

    public void atualizarPaciente(Paciente paciente) {
        String query = """
                UPDATE PACIENTES
                   SET CPF = ?, NOME_COMPLETO = ?, DATA_NASCIMENTO = ?,
                   SEXO = ?, EMAIL = ?, TELEFONE = ?
                   WHERE ID = ?
                """;

        jdbcTemplate.update(query,
                paciente.getCPF(), paciente.getNomeCompleto(), Date.valueOf(paciente.getDataNascimento()),
                paciente.getSexo().toString(), paciente.getEmail(), paciente.getTelefone(), paciente.getId()
        );
    }

    public void deletarPaciente(Long pacienteId){
        String query = """
                DELETE FROM PACIENTES WHERE ID = ?
        """;
        jdbcTemplate.update(query, pacienteId);
    }
}
