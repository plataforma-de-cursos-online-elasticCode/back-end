package br.com.solutis.matricula_service.dto;

public record MatriculaRequestDto(
        Long alunoId,
        Long cursoId
) {
}
