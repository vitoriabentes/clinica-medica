package br.clinica.medica.service;

import br.clinica.medica.dtos.responses.MedicoResposta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.models.Medico;
import br.clinica.medica.repository.EspecialidadeRepository;
import br.clinica.medica.repository.MedicoEspecialiadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoEspecialidadeService {

    @Autowired
    private MedicoEspecialiadeRepository medicoEspecialiadeRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private MedicoService medicoService;

    public List<MedicoResposta> buscarMedicosAssociados(Long idEspecialidade){
        if(especialidadeRepository.buscarPorId(idEspecialidade) != null){
            List<Medico> medicosAssociados = medicoEspecialiadeRepository.buscarMedicosAssociados(idEspecialidade);

            for(Medico medico : medicosAssociados){
                List<Especialidade> especialidades = medicoEspecialiadeRepository.buscarEspecialidadesDoMedico(medico.getId());
                medico.setEspecialidades(especialidades);
            }

            return medicosAssociados.stream()
                    .map(medicoService::converteMedico).toList();
        }

        return null;
    }
}
