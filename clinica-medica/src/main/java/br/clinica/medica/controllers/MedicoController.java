package br.clinica.medica.controllers;

import br.clinica.medica.dtos.requests.MedicoRequisicao;
import br.clinica.medica.dtos.responses.MedicoResposta;
import br.clinica.medica.models.Medico;
import br.clinica.medica.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<?> cadastraMedico(@RequestBody MedicoRequisicao medicoRequisicao){
        try{
            Medico medico = medicoService.cadastraMedico(medicoRequisicao);
            MedicoResposta medicoResposta = medicoService.converteMedico(medico);
            return ResponseEntity.status(HttpStatus.CREATED).body(medicoResposta);
        }catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("Erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarMedicos(@RequestParam Optional<String> ordenarPor){
        return ResponseEntity.status(HttpStatus.OK).body(medicoService.buscaMedicos(ordenarPor));
    }
}
