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
    private MedicoEspecialiadeRepository medicoEspecialidadeRepository;

    @Autowired
    private MedicoEspecialidadeService medicoEspecialidadeService;

    @Autowired
    private EspecialidadeService especialidadeService;

    public void cadastrarMedico(MedicoRequisicao medicoRequisicao){
        validarValoresUnicos(medicoRequisicao);

        Set<Especialidade> especialidades = new HashSet<>();
        for (Long id : medicoRequisicao.especialidades()){
            Especialidade especialidade = especialidadeRepository.buscarPorId(id);
            especialidades.add(especialidade);
        }

        Medico medico = converterMedicoRequisicao(medicoRequisicao, especialidades);

        medico = medicoRepository.cadastraMedico(medico);

        for (Long especialidadeId : medicoRequisicao.especialidades()){
            medicoEspecialidadeService.associarEspecialidade(medico.getId(), especialidadeId);
        }
    }

    public List<MedicoResposta> buscarMedicos(Optional<String> ordenarPor){
        List<Medico> medicosCadastrados = medicoRepository.buscaMedicos(ordenarPor);
        for(Medico medico : medicosCadastrados){
            List<Especialidade> especialidades = medicoEspecialidadeRepository.buscarEspecialidadesDoMedico(medico.getId());
            medico.setEspecialidades(especialidades);
        }
        return medicosCadastrados.stream()
                .map(this::converterMedico).toList();
    }

    public MedicoResposta buscarMedicoPorID(Long id){
        Medico medico = medicoRepository.buscarMedicoPorID(id);
        List<Especialidade> especialidadesDoMedico = medicoEspecialidadeRepository.buscarEspecialidadesDoMedico(medico.getId());
        medico.setEspecialidades(especialidadesDoMedico);
        return converterMedico(medico);
    }

    public void atualizarMedico(Long id, MedicoRequisicao medicoAtualizado){
        Medico medico = medicoRepository.buscarMedicoPorID(id);
        List<Especialidade> especialidadesDoMedico = medicoEspecialidadeRepository.buscarEspecialidadesDoMedico(medico.getId());
        medico.setEspecialidades(especialidadesDoMedico);

        validarValoresUnicosParaAtualizacao(id, medicoAtualizado);

        medico.setCRM(medicoAtualizado.CRM());
        medico.setCPF(medicoAtualizado.CPF());
        medico.setNomeCompleto(medicoAtualizado.nomeCompleto());
        medico.setDataNascimento(medicoAtualizado.dataNascimento());
        medico.setSexo(medicoAtualizado.sexo());
        medico.setEmail(medicoAtualizado.email());
        medico.setTelefone(medicoAtualizado.telefone());
        medico.setStatus(medicoAtualizado.status());

        medicoRepository.atualizarMedico(medico);

        if(!medicoEspecialidadeService.verificarEspecialidadesAtualizadas(id, medicoAtualizado.especialidades())){
            Set<Long> especialidadesAntigas = medico.getEspecialidades().stream().map(Especialidade::getId).collect(Collectors.toSet());
            for(Long idEspecialidade : especialidadesAntigas){
                medicoEspecialidadeRepository.dessasociarEspecialidade(idEspecialidade, id);
            }
            for(Long idEspecialidade : medicoAtualizado.especialidades()){
                medicoEspecialidadeService.associarEspecialidade(idEspecialidade, id);
            }
        }
    }

    public void deletarMedico(Long id) {
        List<Especialidade> especialidadesAssociadas = medicoEspecialidadeRepository.buscarEspecialidadesDoMedico(id);
        for(Especialidade  especialidade : especialidadesAssociadas) {
            medicoEspecialidadeRepository.dessasociarEspecialidade(especialidade.getId(), id);
        }
        medicoRepository.deletarMedico(id);
    }

    private void validarValoresUnicos(MedicoRequisicao medicoRequisicao){
        if(medicoRepository.buscarMedicoPorCRM(medicoRequisicao.CRM()).isPresent()){
            throw new RuntimeException("Esse CRM já está cadastro no sistema");
        }
        if(medicoRepository.buscarMedicoPorCPF(medicoRequisicao.CPF()).isPresent()){
            throw new RuntimeException("Esse médico já está cadastrado no sistema");
        }
        if(medicoRepository.buscarMedicoPorEmail(medicoRequisicao.email()).isPresent()){
            throw new RuntimeException("Esse e-mail já está cadastro no sistema");
        }
    }

    private void validarValoresUnicosParaAtualizacao(Long idMedico, MedicoRequisicao medicoRequisicao){
        Optional<Medico> medicoPorCRM = medicoRepository.buscarMedicoPorCRM(medicoRequisicao.CRM());
        if(medicoPorCRM.isPresent() && !medicoPorCRM.get().getId().equals(idMedico)){
            throw new RuntimeException("Esse CRM já está cadastro no sistema");
        }
        Optional<Medico> medicoPorCPF = medicoRepository.buscarMedicoPorCPF(medicoRequisicao.CPF());
        if(medicoPorCPF.isPresent() && !medicoPorCPF.get().getId().equals(idMedico)){
            throw new RuntimeException("Esse médico já está cadastrado no sistema");
        }
        Optional<Medico> medicoPorEmail = medicoRepository.buscarMedicoPorEmail(medicoRequisicao.email());
        if(medicoPorEmail.isPresent() && !medicoPorEmail.get().getId().equals(idMedico)){
            throw new RuntimeException("Esse e-mail já está cadastro no sistema");
        }
    }

    private Medico converterMedicoRequisicao(MedicoRequisicao medicoRequisicao, Set<Especialidade> especialidades){
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

    public MedicoResposta converterMedico(Medico medico){
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
