package br.clinica.medica.controllers;

import br.clinica.medica.dtos.requests.MedicoRequisicao;
import br.clinica.medica.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<?> cadastraMedico(@RequestBody @Valid MedicoRequisicao medicoRequisicao){
        medicoService.cadastrarMedico(medicoRequisicao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> buscarMedicos(){
        return ResponseEntity.status(HttpStatus.OK).body(medicoService.buscarMedicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMedicoPorID(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(medicoService.buscarMedicoPorID(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMedico(@PathVariable Long id){
        medicoService.deletarMedico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMedico(@PathVariable Long id, @RequestBody @Valid MedicoRequisicao medicoRequisicao){
        medicoService.atualizarMedico(id, medicoRequisicao);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
