package br.clinica.medica.controllers;

import br.clinica.medica.dtos.requests.EspecialidadeRequisicao;
import br.clinica.medica.dtos.responses.EspecialidadeResposta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @PostMapping
    public ResponseEntity<?> cadastraEspecialidade(@RequestBody EspecialidadeRequisicao especialidadeRequisicao){
        try{
            Especialidade especialidade = especialidadeService.cadastraEspecialidade(especialidadeRequisicao);
            EspecialidadeResposta especialidadeResposta = especialidadeService.converteEspecialidade(especialidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeResposta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("Erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> buscaEspecialidades(@RequestParam Optional<String> ordenarPor){
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeService.buscaEspecialidades(ordenarPor));
    }

}
