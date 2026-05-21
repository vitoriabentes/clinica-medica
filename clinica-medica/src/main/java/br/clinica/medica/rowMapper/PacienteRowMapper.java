package br.clinica.medica.rowMapper;

import br.clinica.medica.models.Paciente;
import br.clinica.medica.models.enums.Sexo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PacienteRowMapper implements RowMapper<Paciente> {

    @Override
    public Paciente mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(resultSet.getLong("ID"));
        paciente.setCPF(resultSet.getString("CPF"));
        paciente.setNomeCompleto(resultSet.getString("NOME_COMPLETO"));
        paciente.setDataNascimento(resultSet.getDate("DATA_NASCIMENTO").toLocalDate());
        paciente.setSexo(Sexo.valueOf(resultSet.getString("SEXO")));
        paciente.setEmail(resultSet.getString("EMAIL"));
        paciente.setTelefone(resultSet.getString("TELEFONE"));
        return paciente;
    }
}
