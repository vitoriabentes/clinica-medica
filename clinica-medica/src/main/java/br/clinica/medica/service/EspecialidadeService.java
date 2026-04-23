package br.clinica.medica.service;

import br.clinica.medica.dtos.requests.EspecialidadeRequisicao;
import br.clinica.medica.dtos.responses.EspecialidadeResposta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

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
