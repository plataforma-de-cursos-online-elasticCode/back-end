package br.com.solutis.matricula_service.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class MatriculaResponseDto {

    private Long id;
    private String nomeAluno;
    private String nomeCurso;
    private String status;
    private String dataMatricula;

}
