package br.clinica.medica.controllers;

import br.clinica.medica.dtos.requests.EspecialidadeRequisicao;
import br.clinica.medica.dtos.responses.EspecialidadeResposta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @PostMapping
    public ResponseEntity<?> cadastraEspecialidade(@RequestBody @Valid EspecialidadeRequisicao especialidadeRequisicao){
        Especialidade especialidade = especialidadeService.cadastraEspecialidade(especialidadeRequisicao);
        EspecialidadeResposta especialidadeResposta = especialidadeService.converteEspecialidade(especialidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeResposta);
    }

    @GetMapping
    public ResponseEntity<?> buscaEspecialidades(@RequestParam Optional<String> ordenarPor){
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeService.buscaEspecialidades(ordenarPor));
    }

}
