package br.com.solutis.usuario_service.dto;

import br.com.solutis.usuario_service.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioTokenResponseDto {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private String token;


}
