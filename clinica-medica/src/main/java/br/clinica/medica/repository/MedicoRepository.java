package br.clinica.medica.repository;

import br.clinica.medica.exceptions.ResourceNotFoundException;
import br.clinica.medica.models.Medico;
import br.clinica.medica.rowMapper.MedicoRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicoRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MedicoRowMapper medicoRowMapper;

    public Optional<Medico> buscarMedicoPorCRM(String CRM){
        String query = """
                SELECT * FROM MEDICOS WHERE CRM = ?
                """;
        try {
            Medico medico = jdbcTemplate.queryForObject(query, medicoRowMapper, CRM);
            return Optional.ofNullable(medico);

        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Optional<Medico> buscarMedicoPorCPF(String CPF){
        String query = """
                SELECT * FROM MEDICOS WHERE CPF = ?
                """;

        try {
            Medico medico = jdbcTemplate.queryForObject(query, medicoRowMapper, CPF);
            return Optional.ofNullable(medico);

        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Optional<Medico> buscarMedicoPorEmail(String email){
        String query = """
                SELECT * FROM MEDICOS WHERE EMAIL = ?
                """;

        try {
            Medico medico = jdbcTemplate.queryForObject(query, medicoRowMapper, email);
            return Optional.ofNullable(medico);

        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Medico buscarMedicoPorID(Long id){
        String query = """
                SELECT * FROM MEDICOS WHERE ID = ?
                """;
        try{
            return jdbcTemplate.queryForObject(query, medicoRowMapper, id);
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Médico não cadastrado no sistema.");
        }
    }

    public List<Medico> buscaMedicos(Optional<String> ordenarPor){
        String parametroDeOrdenacao = ordenarPor.orElse("NOME_COMPLETO");
        String query = """
                SELECT * FROM MEDICOS ORDER BY        
                """
                + parametroDeOrdenacao;

        return jdbcTemplate.query(query, medicoRowMapper);
    }

    public Medico cadastraMedico(Medico medico){
        LocalDateTime now = LocalDateTime.now();
        medico.setCriadoEm(now);
        medico.setAtualizadoEm(now);

        String query = """
                INSERT INTO MEDICOS (CRM, CPF, NOME_COMPLETO, DATA_NASCIMENTO, SEXO, EMAIL, TELEFONE, STATUS)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, medico.getCRM());
            ps.setString(2, medico.getCPF());
            ps.setString(3, medico.getNomeCompleto());
            ps.setDate(4, Date.valueOf(medico.getDataNascimento()));
            ps.setString(5, medico.getSexo().toString());
            ps.setString(6, medico.getEmail());
            ps.setString(7, medico.getTelefone());
            ps.setString(8, medico.getStatus().toString());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            Long id = keyHolder.getKey().longValue();
            medico.setId(id);
        }

        return medico;
    }

    public void atualizarMedico(Medico medico){
        String query = """
                UPDATE MEDICOS
                   SET CRM = ?, CPF = ?, NOME_COMPLETO = ?, DATA_NASCIMENTO = ?,
                   SEXO = ?, EMAIL = ?, TELEFONE = ?, STATUS = ?
                   WHERE ID = ?
                """;

        jdbcTemplate.update(query,
                medico.getCRM(), medico.getCPF(), medico.getNomeCompleto(), Date.valueOf(medico.getDataNascimento()),
                medico.getSexo().toString(), medico.getEmail(), medico.getTelefone(),
                medico.getStatus().toString(), medico.getId());
    }

    public void deletarMedico(Long medicoId){
        String query = """
                DELETE FROM MEDICOS WHERE ID = ?
        """;
        jdbcTemplate.update(query, medicoId);
    }

}
