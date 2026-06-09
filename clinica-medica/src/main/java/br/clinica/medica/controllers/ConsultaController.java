package br.clinica.medica.controllers;

import br.clinica.medica.dtos.requests.ConsultaRequisicao;
import br.clinica.medica.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<?> agendarConsulta(@RequestBody ConsultaRequisicao consultaRequisicao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.agendarConsulta(consultaRequisicao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarConsulta(@PathVariable Long id) {
        consultaService.cancelarConsulta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("medico/{id}")
    public ResponseEntity<?> consultarAgendaMedico(@PathVariable Long medicoId){
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.buscarAgendaMedico(medicoId));
    }

    @GetMapping("paciente/{id}")
    public ResponseEntity<?> consultarAgendaPaciente(@PathVariable Long pacienteId){
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.buscarAgendaPaciente(pacienteId));
    }

    @PatchMapping("confirmar-consulta/{id}")
    public ResponseEntity<?> confirmarConsulta(@PathVariable Long id) {
        consultaService.confirmarConsulta(id);
        return ResponseEntity.ok().body("Consulta confirmada com sucesso.");
    }

    @PutMapping("reagendar/{id}")
    public ResponseEntity<?> reagendarConsulta(@RequestBody ConsultaRequisicao consultaRequisicao, @PathVariable Long id) {
        return ResponseEntity.ok(consultaService.reagendarConsulta(consultaRequisicao, id));
    }
}
