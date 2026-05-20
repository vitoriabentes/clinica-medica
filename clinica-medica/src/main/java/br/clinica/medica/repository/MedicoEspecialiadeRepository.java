package br.clinica.medica.repository;

import br.clinica.medica.models.Especialidade;
import br.clinica.medica.models.Medico;
import br.clinica.medica.rowMapper.EspecialidadeRowMapper;
import br.clinica.medica.rowMapper.MedicoRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicoEspecialiadeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EspecialidadeRowMapper especialidadeRowMapper;

    @Autowired
    private MedicoRowMapper medicoRowMapper;

    public void associarEspecialidade(Long especialidadeId, Long medicoId) {
        String query = """
                INSERT INTO MEDICO_ESPECIALIDADE (MEDICO_ID, ESPECIALIDADE_ID)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(query, medicoId, especialidadeId);
    }

    public List<Especialidade> buscarEspecialidadesDoMedico(Long idMedico){
        String query = """
                SELECT E.* FROM ESPECIALIDADES E
                JOIN MEDICO_ESPECIALIDADE ME ON ME.ESPECIALIDADE_ID = E.ID
                WHERE ME.MEDICO_ID = ?
                """;

        return jdbcTemplate.query(query, especialidadeRowMapper, idMedico);
    }

    public List<Medico> buscarMedicosAssociados(Long idEspecialidade){
        String query = """
                SELECT M.* FROM MEDICOS M
                JOIN MEDICO_ESPECIALIDADE ME ON ME.MEDICO_ID = M.ID
                WHERE ME.ESPECIALIDADE_ID = ?
                """;

        return jdbcTemplate.query(query, medicoRowMapper, idEspecialidade);
    }

    public void dessasociarEspecialidade(Long especialidadeId, Long medicoId) {
        String query = """
                DELETE FROM MEDICO_ESPECIALIDADE
                WHERE MEDICO_ID = ? AND ESPECIALIDADE_ID = ?
                """;

        jdbcTemplate.update(query, medicoId, especialidadeId);
    }
}
