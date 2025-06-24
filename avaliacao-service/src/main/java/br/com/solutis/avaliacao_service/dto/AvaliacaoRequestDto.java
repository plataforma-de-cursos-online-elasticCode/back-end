package br.com.solutis.avaliacao_service.dto;

import lombok.Getter;

@Getter
public class AvaliacaoRequestDto {
    private Long alunoId;
    private Long cursoId;
    private Double nota;
    private String comentario;
}
