package br.com.solutis.usuario_service.controller;

import br.com.solutis.usuario_service.dto.*;
import br.com.solutis.usuario_service.entity.Usuario;
import br.com.solutis.usuario_service.mapper.UsuarioMapper;
import br.com.solutis.usuario_service.service.TokenService;
import br.com.solutis.usuario_service.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final TokenService tokenService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody @Valid UsuarioRequestDto dto){
        Usuario usuario = UsuarioMapper.toEntity(dto);
        Usuario usuarioSalvo = service.cadastrarUsuario(usuario);
        UsuarioResponseDto response = UsuarioMapper.toResponseDto(usuarioSalvo);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenResponseDto> login(@RequestBody @Valid UsuarioLoginDto dto) {
        Usuario usuario = UsuarioMapper.toEntityLogin(dto);
        Usuario usuarioAutenticado = service.autenticar(usuario);
        String token = tokenService.gerarToken(usuarioAutenticado);
        UsuarioTokenResponseDto response = UsuarioMapper.toTokenResponseDto(usuarioAutenticado, token);
        return ResponseEntity.status(200).body(response);
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        List<Usuario> usuarios = service.listarUsuario();
        List<UsuarioResponseDto> usuariosResponse = usuarios.stream()
                .map(UsuarioMapper::toResponseDto)
                .toList();

        if (usuariosResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuariosResponse);
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
