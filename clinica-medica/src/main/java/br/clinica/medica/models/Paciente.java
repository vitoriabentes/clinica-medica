package br.clinica.medica.models;

import br.clinica.medica.models.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    private Long id;
    private String CPF;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String telefone;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Paciente(String CPF, String nomeCompleto, LocalDate dataNascimento, Sexo sexo, String email, String telefone) {
        this.CPF = CPF;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.email = email;
        this.telefone = telefone;
    }
}
