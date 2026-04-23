package br.clinica.medica.models;

import br.clinica.medica.models.enums.Sexo;
import br.clinica.medica.models.enums.StatusProfissional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {
    private Long id;
    private String CRM;
    private String CPF;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String telefone;
    private StatusProfissional status;
    private List<Especialidade> especialidades;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Medico(String CRM, String CPF, String nomeCompleto, LocalDate dataNascimento, Sexo sexo, String email,
                  String telefone, StatusProfissional status, List<Especialidade> especialidades) {
        this.CRM = CRM;
        this.CPF = CPF;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.email = email;
        this.telefone = telefone;
        this.status = status;
        this.especialidades = especialidades;
    }
}
