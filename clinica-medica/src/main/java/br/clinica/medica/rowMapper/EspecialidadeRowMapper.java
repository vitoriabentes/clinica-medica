package br.clinica.medica.rowMapper;

import br.clinica.medica.models.Especialidade;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EspecialidadeRowMapper implements RowMapper<Especialidade> {

    @Override
    public Especialidade mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Especialidade especialidade = new Especialidade();

        especialidade.setId(resultSet.getLong("ID"));
        especialidade.setNome(resultSet.getString("NOME"));
        especialidade.setDescricao(resultSet.getString("DESCRICAO"));
        especialidade.setCriadoEm(resultSet.getTimestamp("CRIADO_EM").toLocalDateTime());
        especialidade.setAtualizadoEm(resultSet.getTimestamp("ATUALIZADO_EM").toLocalDateTime());

        return especialidade;
    }
}