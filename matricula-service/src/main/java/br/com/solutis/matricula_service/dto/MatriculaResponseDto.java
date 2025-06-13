package br.com.solutis.matricula_service.dto;

public record MatriculaResponseDto(
        Long id,
        String nomeAluno,
        String nomeCurso,
        String status,
        String dataMatricula
) {
}
