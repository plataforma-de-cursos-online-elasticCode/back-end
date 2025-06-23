package br.com.solutis.usuario_service.service;

import br.com.solutis.usuario_service.entity.Usuario;
import br.com.solutis.usuario_service.exception.DadosInvalidosException;
import br.com.solutis.usuario_service.exception.EntidadeConflitoException;
import br.com.solutis.usuario_service.exception.EntidadeNaoEncontrada;
import br.com.solutis.usuario_service.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario){
        if (usuario.getEmail() == null){
            throw new DadosInvalidosException("Dados inválidos para cadastro do usuário");
        }

        if (usuario.getId() != null && repository.existsById(usuario.getId()) || repository.existsByEmail(usuario.getEmail())) {
            throw new EntidadeConflitoException("Usuário já cadastrado com o ID: " + usuario.getId());
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return this.repository.save(usuario);
    }

    public Usuario autenticar(Usuario usuario){
        if (usuario.getEmail() == null || usuario.getSenha() == null) {
            throw new DadosInvalidosException("Email e senha são obrigatórios para autenticação.");
        }

        Usuario usuarioExistente = repository.findByEmail(usuario.getEmail())
                .filter(user -> passwordEncoder.matches(usuario.getSenha(), user.getSenha()))
                .orElseThrow(() -> new EntidadeNaoEncontrada("Usuário não encontrado com o email: " + usuario.getEmail()));

        if (!passwordEncoder.matches(usuario.getSenha(), usuarioExistente.getSenha())) {
            throw new DadosInvalidosException("Senha incorreta para o usuário: " + usuario.getEmail());
        }

        return usuarioExistente;
    }

    public List<Usuario> listarUsuario() {
        List<Usuario> usuarios = repository.findAll();
        if (usuarios == null || usuarios.isEmpty()) {
            throw new EntidadeNaoEncontrada("Nenhum usuário encontrado.");
        }
        return usuarios;
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontrada("Usuário não encontrado com o ID: " + id));
    }

    public Usuario alterarUsuario(Usuario usuario, Long id) {
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontrada("Usuário não encontrado com o ID: " + id));

        if (usuario.getEmail() != null) {
            usuarioExistente.setEmail(usuario.getEmail());
        }

        if (usuario.getNome() != null) {
            usuarioExistente.setNome(usuario.getNome());
        }

        return repository.save(usuarioExistente);
    }

    public void removerUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontrada("Usuário não encontrado com o ID: " + id);
        }

        repository.deleteById(id);
    }
}


