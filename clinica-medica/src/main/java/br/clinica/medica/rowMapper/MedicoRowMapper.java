package br.clinica.medica.rowMapper;

import br.clinica.medica.models.Medico;
import br.clinica.medica.models.enums.Sexo;
import br.clinica.medica.models.enums.StatusProfissional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MedicoRowMapper implements RowMapper<Medico> {

    @Override
    public Medico mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Medico medico = new Medico();

        medico.setId(resultSet.getLong("ID"));
        medico.setCRM(resultSet.getString("CRM"));
        medico.setCPF(resultSet.getString("CPF"));
        medico.setNomeCompleto(resultSet.getString("NOME_COMPLETO"));
        medico.setDataNascimento(resultSet.getDate("DATA_NASCIMENTO").toLocalDate());
        medico.setSexo(Sexo.valueOf(resultSet.getString("SEXO")));
        medico.setEmail(resultSet.getString("EMAIL"));
        medico.setTelefone(resultSet.getString("TELEFONE"));
        medico.setStatus(StatusProfissional.valueOf(resultSet.getString("STATUS")));

        return medico;
    }
}
