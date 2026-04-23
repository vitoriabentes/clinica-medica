package br.clinica.medica.repository;

import br.clinica.medica.models.Especialidade;
import br.clinica.medica.rowMapper.EspecialidadeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class EspecialidadeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EspecialidadeRowMapper especialidadeRowMapper;

    public Optional<Especialidade> buscarPorNome(String nome){
        String query = """
                SELECT * FROM ESPECIALIDADES WHERE NOME = ?
                """;
        try {
            Especialidade especialidade = jdbcTemplate.queryForObject(query, especialidadeRowMapper, nome.toUpperCase());
            return Optional.ofNullable(especialidade);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Especialidade> buscarPorId(Long id){
        String query = """
                SELECT * FROM ESPECIALIDADES WHERE ID = ?
                """;
        try {
            Especialidade especialidade = jdbcTemplate.queryForObject(query, especialidadeRowMapper, id);
            return Optional.ofNullable(especialidade);
        }catch (RuntimeException e){
            return Optional.empty();
        }
    }

    public  List<Especialidade> buscarEspecialidadesCadastradas(Optional<String> ordenarPor){
        String parametroDeOrdenacao = ordenarPor.orElse("NOME");
        String query = """
                SELECT * FROM ESPECIALIDADES ORDER BY
                """
                + parametroDeOrdenacao;
        return jdbcTemplate.query(query, especialidadeRowMapper);
    }

    public Especialidade salvarEspecialidade(Especialidade especialidade){
        LocalDateTime now = LocalDateTime.now();
        especialidade.setCriadoEm(now);
        especialidade.setAtualizadoEm(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = """
            INSERT INTO ESPECIALIDADES (NOME, DESCRICAO, CRIADO_EM, ATUALIZADO_EM)
            VALUES (?, ?, ?, ?)
            """;
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, especialidade.getNome().toUpperCase());
            ps.setString(2, especialidade.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(especialidade.getCriadoEm()));
            ps.setTimestamp(4, Timestamp.valueOf(especialidade.getAtualizadoEm()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            Long id = keyHolder.getKey().longValue();
            especialidade.setId(id);
        }
        return especialidade;
    }
}
