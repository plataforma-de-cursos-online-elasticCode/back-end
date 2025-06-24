package br.com.solutis.certificado_service.dto.cursodto;

import lombok.Data;

@Data
public class CursoResponseDto {

    private String nome;
    private String descricao;
    private Double preco;
    private String categoria;

}
