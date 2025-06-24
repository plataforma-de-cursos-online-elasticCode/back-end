package br.com.solutis.usuario_service.service;

import br.com.solutis.usuario_service.entity.Usuario;
import br.com.solutis.usuario_service.exception.DadosInvalidosException;
import br.com.solutis.usuario_service.exception.EntidadeConflitoException;
import br.com.solutis.usuario_service.exception.EntidadeNaoEncontrada;
import br.com.solutis.usuario_service.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("123456");
        usuario.setNome("Usuario Teste");
    }

    @Test
    @DisplayName("Cadastrar usuário válido com sucesso")
    void cadastrarUsuarioComSucesso() {
        when(repository.existsById(usuario.getId())).thenReturn(false);
        when(repository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(usuario.getSenha())).thenReturn("senha_codificada");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = service.cadastrarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(usuario.getEmail(), resultado.getEmail());
        verify(passwordEncoder).encode("123456");
        verify(repository).save(usuario);
    }

    @Test
    @DisplayName("Cadastrar usuário com email nulo lança DadosInvalidosException")
    void cadastrarUsuarioEmailNulo() {
        usuario.setEmail(null);
        assertThrows(DadosInvalidosException.class, () -> service.cadastrarUsuario(usuario));
    }

    @Test
    @DisplayName("Cadastrar usuário já existente lança EntidadeConflitoException")
    void cadastrarUsuarioExistente() {
        when(repository.existsById(usuario.getId())).thenReturn(true);
        assertThrows(EntidadeConflitoException.class, () -> service.cadastrarUsuario(usuario));
    }

    @Test
    @DisplayName("Autenticar usuário com sucesso")
    void autenticarUsuarioComSucesso() {
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(usuario.getSenha(), usuario.getSenha())).thenReturn(true);

        Usuario resultado = service.autenticar(usuario);

        assertEquals(usuario.getEmail(), resultado.getEmail());
    }

    @Test
    @DisplayName("Autenticar usuário com senha inválida lança EntidadeNaoEncontrada")
    void autenticarUsuarioSenhaInvalida() {
        Usuario usuarioLogin = new Usuario();
        usuarioLogin.setEmail("teste@teste.com");
        usuarioLogin.setSenha("senha-invalida");

        Usuario usuarioNoBanco = new Usuario();
        usuarioNoBanco.setEmail("teste@teste.com");
        usuarioNoBanco.setSenha("senha-correta-criptografada");

        when(repository.findByEmail(usuarioLogin.getEmail()))
                .thenReturn(Optional.of(usuarioNoBanco));
        when(passwordEncoder.matches(usuarioLogin.getSenha(), usuarioNoBanco.getSenha()))
                .thenReturn(false);

        assertThrows(EntidadeNaoEncontrada.class, () -> service.autenticar(usuarioLogin));
    }


    @Test
    @DisplayName("Listar usuários retorna lista cheia")
    void listarUsuarios() {
        when(repository.findAll()).thenReturn(List.of(usuario));
        List<Usuario> usuarios = service.listarUsuario();

        assertFalse(usuarios.isEmpty());
    }

    @Test
    @DisplayName("Listar usuários vazia lança EntidadeNaoEncontrada")
    void listarUsuariosVazia() {
        when(repository.findAll()).thenReturn(List.of());
        assertThrows(EntidadeNaoEncontrada.class, () -> service.listarUsuario());
    }

    @Test
    @DisplayName("Buscar usuário por ID com sucesso")
    void buscarUsuarioPorIdSucesso() {
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        Usuario encontrado = service.buscarUsuarioPorId(usuario.getId());
        assertEquals(usuario.getId(), encontrado.getId());
    }

    @Test
    @DisplayName("Buscar usuário por ID inexistente lança EntidadeNaoEncontrada")
    void buscarUsuarioPorIdInexistente() {
        when(repository.findById(usuario.getId())).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontrada.class, () -> service.buscarUsuarioPorId(usuario.getId()));
    }

    @Test
    @DisplayName("Remover usuário existente com sucesso")
    void removerUsuarioSucesso() {
        when(repository.existsById(usuario.getId())).thenReturn(true);
        service.removerUsuario(usuario.getId());
        verify(repository).deleteById(usuario.getId());
    }

    @Test
    @DisplayName("Remover usuário inexistente lança EntidadeNaoEncontrada")
    void removerUsuarioInexistente() {
        when(repository.existsById(usuario.getId())).thenReturn(false);
        assertThrows(EntidadeNaoEncontrada.class, () -> service.removerUsuario(usuario.getId()));
    }
}