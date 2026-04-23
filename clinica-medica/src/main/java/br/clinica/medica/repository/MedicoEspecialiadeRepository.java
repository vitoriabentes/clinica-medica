package br.clinica.medica.repository;

import br.clinica.medica.models.Especialidade;
import br.clinica.medica.models.Medico;
import br.clinica.medica.rowMapper.EspecialidadeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class MedicoEspecialiadeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EspecialidadeRowMapper especialidadeRowMapper;

    public void cadastroEspecialidadesDoMedico(Medico medico){
        String query = """
                INSERT INTO MEDICO_ESPECIALIDADE (MEDICO_ID, ESPECIALIDADE_ID)
                VALUES (?, ?)
                """;

        for(Especialidade especialidade : medico.getEspecialidades()){
            jdbcTemplate.update(query, medico.getId(), especialidade.getId());
        }
    }

    public List<Especialidade> buscarEspecialidadesDoMedico(Long idMedico){
        String query = """
                SELECT E.* FROM ESPECIALIDADES e
                JOIN MEDICO_ESPECIALIDADE ME ON ME.ESPECIALIDADE_ID = E.ID
                WHERE ME.MEDICO_ID = ?
                """;

        return jdbcTemplate.query(query, especialidadeRowMapper, idMedico);
    }
}
