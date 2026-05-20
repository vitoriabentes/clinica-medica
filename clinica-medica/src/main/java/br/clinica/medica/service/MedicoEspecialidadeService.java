package br.clinica.medica.service;

import br.clinica.medica.exceptions.ResourceNotFoundException;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.models.Medico;
import br.clinica.medica.repository.EspecialidadeRepository;
import br.clinica.medica.repository.MedicoEspecialiadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MedicoEspecialidadeService {

    @Autowired
    private MedicoEspecialiadeRepository medicoEspecialiadeRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public List<Medico> buscarMedicosAssociados(Long idEspecialidade){
        if(especialidadeRepository.buscarPorId(idEspecialidade) != null){
            List<Medico> medicosAssociados = medicoEspecialiadeRepository.buscarMedicosAssociados(idEspecialidade);

            for(Medico medico : medicosAssociados){
                List<Especialidade> especialidades = medicoEspecialiadeRepository.buscarEspecialidadesDoMedico(medico.getId());
                medico.setEspecialidades(especialidades);
            }

            return medicosAssociados;
        }

        return null;
    }

    public void associarEspecialidade(Long especialidadeId, Long medicoId){
        if(especialidadeRepository.buscarPorId(especialidadeId) == null){
            throw new ResourceNotFoundException(String.format("Especialidade com o id %d não cadastrada no sistema.", especialidadeId));
        }

        medicoEspecialiadeRepository.associarEspecialidade(especialidadeId, medicoId);
    }

    public boolean verificarEspecialidadesAtualizadas(Long idMedico, Set<Long> especialidadesAtualizadas){
        List<Especialidade> especialidadesMedico = medicoEspecialiadeRepository.buscarEspecialidadesDoMedico(idMedico);
        Set<Long> idsEspecialidades = especialidadesMedico.stream().map(Especialidade::getId).collect(java.util.stream.Collectors.toSet());
        return idsEspecialidades.equals(especialidadesAtualizadas);
    }
}
