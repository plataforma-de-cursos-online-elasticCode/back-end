package br.com.solutis.usuario_service.controller;

import br.com.solutis.usuario_service.dto.UsuarioRequestDto;
import br.com.solutis.usuario_service.dto.UsuarioResponseDto;
import br.com.solutis.usuario_service.dto.UsuarioUpdateDto;
import br.com.solutis.usuario_service.entity.Usuario;
import br.com.solutis.usuario_service.mapper.UsuarioMapper;
import br.com.solutis.usuario_service.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody @Valid UsuarioRequestDto dto){
        Usuario usuario = UsuarioMapper.toEntity(dto);
        service.cadastrarUsuario(usuario);
        UsuarioResponseDto usuarioSalvo = UsuarioMapper.toResponseDto(usuario);
        return ResponseEntity.status(201).body(usuarioSalvo);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar(){
        List<Usuario> usuarios = service.listarUsuario();
        List<UsuarioResponseDto> usuariosResponse = usuarios.stream().map(UsuarioMapper::toResponseDto).toList();
        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(usuariosResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Long id) {
        Usuario usuario = service.buscarUsuarioPorId(id);
        UsuarioResponseDto usuarioResponse = UsuarioMapper.toResponseDto(usuario);
        return ResponseEntity.status(200).body(usuarioResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateDto dto) {
        Usuario usuario = UsuarioMapper.toUpdate(dto, id);
        Usuario usuarioAlterado = service.alterarUsuario(usuario, id);
        UsuarioResponseDto usuarioDto = UsuarioMapper.toResponseDto(usuarioAlterado);
        return ResponseEntity.status(200).body(usuarioDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.removerUsuario(id);
        return ResponseEntity.status(204).build();
    }

}
