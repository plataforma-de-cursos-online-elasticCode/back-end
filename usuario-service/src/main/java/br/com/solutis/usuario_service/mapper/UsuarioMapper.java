package br.com.solutis.usuario_service.mapper;

import br.com.solutis.usuario_service.dto.UsuarioRequestDto;
import br.com.solutis.usuario_service.dto.UsuarioResponseDto;
import br.com.solutis.usuario_service.dto.UsuarioUpdateDto;
import br.com.solutis.usuario_service.entity.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDto dto){
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTipoUsuario(dto.getTipoUsuario());

        return usuario;
    }

    public static UsuarioResponseDto toResponseDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTipoUsuario(usuario.getTipoUsuario());

        return dto;
    }

    public static Usuario toUpdate(UsuarioUpdateDto dto, Long id) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        return usuario;
    }
}
