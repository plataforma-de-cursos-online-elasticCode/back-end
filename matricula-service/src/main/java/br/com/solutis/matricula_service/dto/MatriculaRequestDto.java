package br.com.solutis.matricula_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MatriculaRequestDto {

    private Long alunoId;
    private Long cursoId;

}
