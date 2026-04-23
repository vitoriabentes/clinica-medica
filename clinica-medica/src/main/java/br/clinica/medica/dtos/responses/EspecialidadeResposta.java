package br.clinica.medica.dtos.responses;


import java.time.LocalDateTime;

public record EspecialidadeResposta(
        Long id,
        String nome,
        String descricao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
){}
