package br.clinica.medica.service;

import br.clinica.medica.dtos.requests.PacienteRequisicao;
import br.clinica.medica.dtos.responses.PacienteResposta;
import br.clinica.medica.models.Paciente;
import br.clinica.medica.repository.PacienteRepository;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public PacienteResposta buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.buscarPorId(id);
        return converterPaciente(paciente);
    }

    public PacienteResposta cadastrarPaciente(PacienteRequisicao pacienteRequisicao) {
        if (pacienteRepository.buscarPacientePorCPF(pacienteRequisicao.CPF()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }
        if (pacienteRepository.buscarPacientePorEmail(pacienteRequisicao.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado no sistema.");
        }

        Paciente paciente = converterPacienteRequisicao(pacienteRequisicao);
        paciente = pacienteRepository.cadastrarPaciente(paciente);
        return converterPaciente(paciente);
    }

    private PacienteResposta converterPaciente(Paciente paciente) {
        return new PacienteResposta(
                paciente.getCPF(),
                paciente.getNomeCompleto(),
                paciente.getDataNascimento(),
                paciente.getSexo(),
                paciente.getEmail(),
                paciente.getTelefone()
        );
    }

    private Paciente converterPacienteRequisicao(PacienteRequisicao pacienteRequisicao) {
        return new Paciente(
                pacienteRequisicao.CPF(),
                pacienteRequisicao.nomeCompleto(),
                pacienteRequisicao.dataNascimento(),
                pacienteRequisicao.sexo(),
                pacienteRequisicao.email(),
                pacienteRequisicao.telefone()
        );
    }

    public PacienteResposta atualizarPaciente(Long id, PacienteRequisicao pacienteRequisicao) {
        Paciente paciente = pacienteRepository.buscarPorId(id);

        Optional<Paciente> pacientePorCPF = pacienteRepository.buscarPacientePorCPF(pacienteRequisicao.CPF());
        if (pacientePorCPF.isPresent() && !pacientePorCPF.get().getId().equals(paciente.getId())) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }

        Optional<Paciente> pacientePorEmail = pacienteRepository.buscarPacientePorEmail(pacienteRequisicao.email());
        if (pacientePorEmail.isPresent() && !pacientePorEmail.get().getId().equals(paciente.getId())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema.");
        }

        paciente.setCPF(pacienteRequisicao.CPF());
        paciente.setNomeCompleto(pacienteRequisicao.nomeCompleto());
        paciente.setDataNascimento(pacienteRequisicao.dataNascimento());
        paciente.setSexo(pacienteRequisicao.sexo());
        paciente.setEmail(pacienteRequisicao.email());
        paciente.setTelefone(pacienteRequisicao.telefone());

        pacienteRepository.atualizarPaciente(paciente);
        return converterPaciente(paciente);
    }

    public void deletarPaciente(Long id) {
        //Verificar se há agendamentos desse paciente
        pacienteRepository.deletarPaciente(id);
    }
}