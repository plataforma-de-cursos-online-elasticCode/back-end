package br.com.solutis.avaliacao_service.dto;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class AvaliacaoResponseDto {
    private String nomeAluno;
    private String nomeCurso;
    private Double nota;
    private String comentario;
}
