package br.clinica.medica.service;

import br.clinica.medica.dtos.requests.EspecialidadeRequisicao;
import br.clinica.medica.dtos.responses.EspecialidadeResposta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.models.Medico;
import br.clinica.medica.repository.EspecialidadeRepository;
import br.clinica.medica.repository.MedicoEspecialiadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private MedicoEspecialiadeRepository medicoEspecialiadeRepository;

    public Especialidade cadastraEspecialidade(EspecialidadeRequisicao especialidadeRequisicao) throws RuntimeException{
        if(especialidadeRepository.buscarPorNome(especialidadeRequisicao.nome()).isPresent()){
            throw new RuntimeException("Especialidade já cadastrada no sistema");
        }

        Especialidade especialidade = converteEspecialidadeRequisicao(especialidadeRequisicao);
        return especialidadeRepository.salvarEspecialidade(especialidade);
    }

    public List<EspecialidadeResposta> buscaEspecialidades(Optional<String> ordenarPor){
        List<Especialidade> especialidadesCadastradas = especialidadeRepository.buscarEspecialidadesCadastradas(ordenarPor);
        return especialidadesCadastradas.stream()
                .map(this::converteEspecialidade).toList();
    }

    public EspecialidadeResposta buscaEspecialidadePorID(Long id){
        return converteEspecialidade(especialidadeRepository.buscarPorId(id));
    }

    public EspecialidadeResposta atualizarEspecialidade(Long id, EspecialidadeRequisicao especialidadeAtualizada){
        Especialidade especialidade = especialidadeRepository.buscarPorId(id);
        Optional<Especialidade> especialidadeBuscadaPorNome = especialidadeRepository.buscarPorNome(especialidadeAtualizada.nome());
        if(especialidadeBuscadaPorNome.isPresent() && !especialidadeBuscadaPorNome.get().getId().equals(especialidade.getId())){
            throw new RuntimeException("Nome de especialidade já cadastrada: " + especialidadeAtualizada.nome());
        }

        especialidade.setNome(especialidadeAtualizada.nome());
        especialidade.setDescricao(especialidadeAtualizada.descricao());
        especialidade.setAtualizadoEm(LocalDateTime.now());

        especialidadeRepository.atualizarEspecialidade(especialidade);

        return converteEspecialidade(especialidade);
    }


    public void deletarEspecialidade(Long id){
        List<Medico> medicosAssociados = medicoEspecialiadeRepository.buscarMedicosAssociados(id);
        if(!medicosAssociados.isEmpty()){
            throw new RuntimeException(
                    String.format("Não é possível excluir a especialidade, há %d médico(s) vinculados a esta especialidade",
                            medicosAssociados.size())
            );
        }

        especialidadeRepository.deletarEspecialidade(id);
    }

    private Especialidade converteEspecialidadeRequisicao(EspecialidadeRequisicao especialidadeRequisicao){
        return new Especialidade(
                especialidadeRequisicao.nome(),
                especialidadeRequisicao.descricao()
        );
    }

    public EspecialidadeResposta converteEspecialidade(Especialidade especialidade){
        return new EspecialidadeResposta(
                especialidade.getId(),
                especialidade.getNome(),
                especialidade.getDescricao(),
                especialidade.getCriadoEm(),
                especialidade.getAtualizadoEm()
        );
    }

}
