package br.com.solutis.usuario_service.dto;

import br.com.solutis.usuario_service.TipoUsuario;
import lombok.Data;

@Data
public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
}
