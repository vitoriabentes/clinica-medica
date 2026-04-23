package br.clinica.medica.service;

import br.clinica.medica.dtos.requests.MedicoRequisicao;
import br.clinica.medica.dtos.responses.EspecialidadeResposta;
import br.clinica.medica.dtos.responses.MedicoResposta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.models.Medico;
import br.clinica.medica.repository.EspecialidadeRepository;
import br.clinica.medica.repository.MedicoEspecialiadeRepository;
import br.clinica.medica.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private MedicoEspecialiadeRepository medicoEspecialiadeRepository;

    @Autowired
    private EspecialidadeService especialidadeService;

    public Medico cadastraMedico(MedicoRequisicao medicoRequisicao){
        if(medicoRepository.buscarMedicoPorCRM(medicoRequisicao.CRM()).isPresent()){
            throw new RuntimeException("Esse CRM já está cadastro no sistema");
        }
        if(medicoRepository.buscarMedicoPorCPF(medicoRequisicao.CPF()).isPresent()){
            throw new RuntimeException("Esse médico já está cadastrado no sistema");
        }

        Set<Especialidade> especialidades = new HashSet<>();
        for (Long id : medicoRequisicao.especialidades()){
            try{
                Especialidade especialidade = especialidadeRepository.buscarPorId(id).get();
                especialidades.add(especialidade);

            } catch (RuntimeException e) {
                throw new RuntimeException("Especialidade não existe no sistema");
            }
        }

        Medico medico = converteMedicoRequisicao(medicoRequisicao, especialidades);

        medico = medicoRepository.cadastraMedico(medico);

        medicoEspecialiadeRepository.cadastroEspecialidadesDoMedico(medico);

        return medico;
    }

    public List<MedicoResposta> buscaMedicos(Optional<String> ordenarPor){
        List<Medico> medicosCadastrados = medicoRepository.buscaMedicos(ordenarPor);
        for(Medico medico : medicosCadastrados){
            List<Especialidade> especialidades = medicoEspecialiadeRepository.buscarEspecialidadesDoMedico(medico.getId());
            medico.setEspecialidades(especialidades);
        }
        return medicosCadastrados.stream()
                .map(this::converteMedico).toList();
    }

    public Medico converteMedicoRequisicao(MedicoRequisicao medicoRequisicao, Set<Especialidade> especialidades){
        return new Medico(
                medicoRequisicao.CRM(),
                medicoRequisicao.CPF(),
                medicoRequisicao.nomeCompleto(),
                medicoRequisicao.dataNascimento(),
                medicoRequisicao.sexo(),
                medicoRequisicao.email(),
                medicoRequisicao.telefone(),
                medicoRequisicao.status(),
                especialidades.stream().toList()
        );

    }

    public MedicoResposta converteMedico(Medico medico){
        Set<EspecialidadeResposta> especialidadesResposta = medico.getEspecialidades().stream()
                .map(especialidadeService::converteEspecialidade)
                .collect(Collectors.toSet());
        return new MedicoResposta(
                medico.getCRM(),
                medico.getCPF(),
                medico.getNomeCompleto(),
                medico.getDataNascimento(),
                medico.getSexo(),
                medico.getEmail(),
                medico.getTelefone(),
                medico.getStatus(),
                especialidadesResposta
        );
    }

}
