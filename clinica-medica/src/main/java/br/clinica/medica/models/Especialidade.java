package br.clinica.medica.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Especialidade {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Especialidade(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
