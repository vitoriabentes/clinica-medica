package br.clinica.medica.service;

import br.clinica.medica.dtos.requests.ConsultaRequisicao;
import br.clinica.medica.dtos.responses.ConsultaResposta;
import br.clinica.medica.dtos.responses.EspecialidadeResposta;
import br.clinica.medica.dtos.responses.MedicoResposta;
import br.clinica.medica.dtos.responses.PacienteResposta;
import br.clinica.medica.exceptions.MedicoInativoException;
import br.clinica.medica.models.Consulta;
import br.clinica.medica.models.enums.StatusConsulta;
import br.clinica.medica.models.enums.StatusProfissional;
import br.clinica.medica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultaService{

    private static final int DURACAO_HORAS_CONSULTA = 1;
    private static final LocalTime HORARIO_FUNCIONAMENTO_INICIO = LocalTime.of(7, 0);
    private static final LocalTime HORARIO_FUNCIONAMENTO_FIM = LocalTime.of(19, 0);

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EspecialidadeService especialidadeService;;

    public ConsultaResposta agendarConsulta(ConsultaRequisicao consultaRequisicao){
        validarMedico(consultaRequisicao);
        validarDataHoraConsulta(consultaRequisicao.dataHoraConsultaInicio());
        verificaAgendaMedico(consultaRequisicao.medicoId(), consultaRequisicao.dataHoraConsultaInicio());
        verificaAgendaPaciente(consultaRequisicao.pacienteId(), consultaRequisicao.dataHoraConsultaInicio());

        Consulta consulta = converterParaConsulta(consultaRequisicao);
        consulta = consultaRepository.agendarConsulta(consulta);

        return converterParaConsultaResposta(consulta);
    }

    public void cancelarConsulta(Long id) {
        consultaRepository.cancelarConsulta(id);
    }

    public List<ConsultaResposta> buscarAgendaMedico(Long medicoId){
        if(medicoService.buscarMedicoPorID(medicoId) != null){
            List<Consulta> agendaMedico = consultaRepository.buscarAgendaMedico(medicoId);
            return agendaMedico.stream().map(this::converterParaConsultaResposta).toList();
        }
        return null;
    }

    public List<ConsultaResposta> buscarAgendaPaciente(Long pacienteId) {
        if(pacienteService.buscarPorId(pacienteId) != null){
            List<Consulta> agendaPaciente = consultaRepository.buscarAgendaPaciente(pacienteId);
            return agendaPaciente.stream().map(this::converterParaConsultaResposta).toList();
        }
        return null;
    }

    public void confirmarConsulta(Long id) {
        if(consultaRepository.buscarConsultaPorID(id) != null){
            consultaRepository.confirmarConsulta(id);
        }
    }

    public ConsultaResposta reagendarConsulta(ConsultaRequisicao consultaRequisicao, Long id) {
        if(consultaRepository.buscarConsultaPorID(id) != null){
            validarMedico(consultaRequisicao);
            validarDataHoraConsulta(consultaRequisicao.dataHoraConsultaInicio());
            verificaAgendaMedico(consultaRequisicao.medicoId(), consultaRequisicao.dataHoraConsultaInicio());
            verificaAgendaPaciente(consultaRequisicao.pacienteId(), consultaRequisicao.dataHoraConsultaInicio());

            Consulta consultaAtualizada = converterParaConsulta(consultaRequisicao);
            consultaAtualizada.setId(id);
            consultaRepository.reagendarConsulta(consultaAtualizada);

            return converterParaConsultaResposta(consultaAtualizada);
        }
        return null;
    }

    private void validarMedico(ConsultaRequisicao consultaRequisicao){
        MedicoResposta medico = medicoService.buscarMedicoPorID(consultaRequisicao.medicoId());
        EspecialidadeResposta especialidade = especialidadeService.buscaEspecialidadePorID(consultaRequisicao.especialidadeId());

        if(!medico.status().equals(StatusProfissional.ATIVO)){
            throw new MedicoInativoException(String.format("O médico %s está inativo no sistema.", medico.nomeCompleto()));
        }
        if(medico.especialidades().stream().findAny().filter(e -> e.id().equals(especialidade.id())).isEmpty()){
            throw new RuntimeException(String.format("O médico %s não possui a especialidade %s.", medico.nomeCompleto(), especialidade.nome()));
        }
    }

    private void validarDataHoraConsulta(LocalDateTime dataHoraInicio){
        if(dataHoraInicio.isBefore(LocalDateTime.now())){
            throw new RuntimeException("A data e hora da consulta não pode ser no passado.");
        }
        if(dataHoraInicio.isBefore(LocalDateTime.now().plusMinutes(30))){
            throw new RuntimeException("A consulta deve ser agendada com no máximo 30 minutos de antecedência.");
        }
        if(dataHoraInicio.getHour() < HORARIO_FUNCIONAMENTO_INICIO.getHour()){
            throw new RuntimeException("A consulta não pode ser agendada antes do horário de funcionamento da clínica.");
        }
        if(dataHoraInicio.getHour() > HORARIO_FUNCIONAMENTO_FIM.getHour() - DURACAO_HORAS_CONSULTA) {
            throw new RuntimeException("A consulta não pode ser agendada após o horário de funcionamento da clínica.");
        }
        if(dataHoraInicio.getDayOfWeek().equals(java.time.DayOfWeek.SATURDAY) || dataHoraInicio.getDayOfWeek().equals(java.time.DayOfWeek.SUNDAY)){
            throw new RuntimeException("A consulta não pode ser agendada para finais de semana, a clínica não funciona aos sábados e domingos.");
        }
    }

    private void verificaAgendaMedico(Long idMedico, LocalDateTime dataHoraInicio){
        List<Consulta> consultaConflitante = consultaRepository.buscarAgendaMedico(idMedico).stream()
                .filter(c -> c.getDataHoraConsultaInicio().equals(dataHoraInicio))
                .toList();

        if(!consultaConflitante.isEmpty()){
            throw new RuntimeException(String.format("O médico já possui uma consulta agendada para o horário %s.", dataHoraInicio));
        }
    }

    private void verificaAgendaPaciente(Long idPaciente, LocalDateTime dataHoraInicio){
        List<Consulta> consultaConflitante = consultaRepository.buscarAgendaPaciente(idPaciente).stream()
                .filter(c -> c.getDataHoraConsultaInicio().equals(dataHoraInicio))
                .toList();

        if(!consultaConflitante.isEmpty()){
            throw new RuntimeException(String.format("O paciente já possui uma consulta agendada para o horário %s.", dataHoraInicio));
        }
    }

    private Consulta converterParaConsulta(ConsultaRequisicao consultaRequisicao){
        return new Consulta(
                consultaRequisicao.dataHoraConsultaInicio(),
                consultaRequisicao.dataHoraConsultaInicio().plusHours(DURACAO_HORAS_CONSULTA),
                consultaRequisicao.statusConsulta() == null ? StatusConsulta.AGENDADO : consultaRequisicao.statusConsulta(),
                consultaRequisicao.medicoId(),
                consultaRequisicao.pacienteId(),
                consultaRequisicao.especialidadeId()
        );
    }

    private ConsultaResposta converterParaConsultaResposta(Consulta consulta){
        MedicoResposta medico = medicoService.buscarMedicoPorID(consulta.getMedicoId());
        PacienteResposta paciente = pacienteService.buscarPorId(consulta.getPacienteId());
        EspecialidadeResposta especialidade = especialidadeService.buscaEspecialidadePorID(consulta.getEspecialidadeId());

        return new ConsultaResposta(
                consulta.getId(),
                consulta.getDataHoraConsultaInicio(),
                consulta.getDataHoraConsultaTermino(),
                consulta.getStatusConsulta(),
                medico,
                paciente,
                especialidade
        );
    }
}
