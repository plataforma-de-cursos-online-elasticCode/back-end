package br.com.solutis.matricula_service.dto;

import br.com.solutis.matricula_service.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@AllArgsConstructor
public class MatriculaResponseDto {

    private Long id;
    private String nomeAluno;
    private String nomeCurso;
    private Status status;
    private LocalDate dataMatricula;

}
