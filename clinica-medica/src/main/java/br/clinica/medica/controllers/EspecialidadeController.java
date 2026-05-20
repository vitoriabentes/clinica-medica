package br.clinica.medica.controllers;

import br.clinica.medica.dtos.requests.EspecialidadeRequisicao;
import br.clinica.medica.dtos.responses.EspecialidadeResposta;
import br.clinica.medica.dtos.responses.MedicoResposta;
import br.clinica.medica.models.Especialidade;
import br.clinica.medica.service.EspecialidadeService;
import br.clinica.medica.service.MedicoEspecialidadeService;
import br.clinica.medica.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @Autowired
    private MedicoEspecialidadeService medicoEspecialidadeService;

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<?> cadastrarEspecialidade(@RequestBody @Valid EspecialidadeRequisicao especialidadeRequisicao){
        Especialidade especialidade = especialidadeService.cadastraEspecialidade(especialidadeRequisicao);
        EspecialidadeResposta especialidadeResposta = especialidadeService.converteEspecialidade(especialidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeResposta);
    }

    @GetMapping
    public ResponseEntity<?> buscarEspecialidades(@RequestParam Optional<String> ordenarPor){
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeService.buscaEspecialidades(ordenarPor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEspecialidadePorID(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeService.buscaEspecialidadePorID(id));
    }

    @GetMapping("/medicos-associados/{idEspecialidade}")
    public ResponseEntity<?> buscarMedicosAssociados(@PathVariable Long idEspecialidade){
        List<MedicoResposta> medicosAssociados = medicoEspecialidadeService.buscarMedicosAssociados(idEspecialidade)
                .stream()
                .map(medicoService::converterMedico).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(medicosAssociados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEspecialidade(@PathVariable Long id,
                                                   @Valid @RequestBody EspecialidadeRequisicao especialidadeRequisicao){
        return ResponseEntity.status(HttpStatus.OK)
                .body(especialidadeService.atualizarEspecialidade(id, especialidadeRequisicao));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEspecialidade(@PathVariable Long id) {
        especialidadeService.deletarEspecialidade(id);
        return ResponseEntity.noContent().build();
    }

}
