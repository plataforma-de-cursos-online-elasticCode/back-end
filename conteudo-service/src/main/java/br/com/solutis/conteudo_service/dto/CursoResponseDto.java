package br.com.solutis.conteudo_service.dto;

import lombok.Data;

@Data
public class CursoResponseDto {
    private String nome;

    private String descricao;

    private Double preco;

    private String categoria;
}
