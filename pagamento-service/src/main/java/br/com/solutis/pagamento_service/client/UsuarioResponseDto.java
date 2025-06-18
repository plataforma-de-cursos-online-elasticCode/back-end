package br.com.solutis.pagamento_service.client;

import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Long id;
    private String nome;
    private String email;
}
