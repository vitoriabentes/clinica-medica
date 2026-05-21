package br.clinica.medica.controllers;

import br.clinica.medica.dtos.requests.PacienteRequisicao;
import br.clinica.medica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPacientePorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> cadastrarPaciente(@RequestBody @Valid PacienteRequisicao pacienteRequisicao){
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastrarPaciente(pacienteRequisicao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPaciente(@PathVariable Long id,
                                                    @Valid @RequestBody PacienteRequisicao pacienteRequisicao){
        return ResponseEntity.status(HttpStatus.OK)
                .body(pacienteService.atualizarPaciente(id, pacienteRequisicao));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPaciente(@PathVariable Long id) {
        pacienteService.deletarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}
