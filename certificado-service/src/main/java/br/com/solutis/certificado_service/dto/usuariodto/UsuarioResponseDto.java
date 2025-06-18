package br.com.solutis.certificado_service.dto.usuariodto;

import lombok.Data;

@Data
public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
}
