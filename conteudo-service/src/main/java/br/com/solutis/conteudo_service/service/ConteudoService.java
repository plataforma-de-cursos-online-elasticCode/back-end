package br.com.solutis.conteudo_service.service;

import br.com.solutis.conteudo_service.client.CursoClient;
import br.com.solutis.conteudo_service.dto.ConteudoRequestDto;
import br.com.solutis.conteudo_service.entity.Conteudo;
import br.com.solutis.conteudo_service.exception.ConteudoNaoEncontradoException;
import br.com.solutis.conteudo_service.mapper.ConteudoMapper;
import br.com.solutis.conteudo_service.repository.ConteudoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConteudoService {

    @Autowired
    private ConteudoRepository repository;

    private final CursoClient cursoClient;

    public ConteudoService(CursoClient cursoClient) {
        this.cursoClient = cursoClient;
    }

    public Conteudo cadastrarConteudo(ConteudoRequestDto request) {
        cursoClient.buscarCursoPorId(request.fkCurso());

        Conteudo newConteudo = ConteudoMapper.toEntity(request);

        return repository.save(newConteudo);
    }

    public List<Conteudo> listarTodosConteudos() {
        return repository.findAll();
    }

    public Conteudo buscarConteudoPorId(Long id) {
        Optional<Conteudo> response = repository.findById(id);

        return response.orElseThrow(
                () -> new ConteudoNaoEncontradoException("Conteudo de id %d não encontrado".formatted(id)));
    }

    public void deletarConteudoPorId(Long id) {
        Optional<Conteudo> conteudo = repository.findById(id);

        if (conteudo.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ConteudoNaoEncontradoException("Conteudo de id %d não encontrado".formatted(id));
        }
    }

    public Conteudo atualizarConteudoPorId(Long id, ConteudoRequestDto request) {
        Conteudo conteudoExistente = repository.findById(id).orElseThrow(
                () -> new ConteudoNaoEncontradoException("Conteudo não encontrado"));

        ConteudoMapper.toConteudoAtualizado(conteudoExistente, request);

        return repository.save(conteudoExistente);
    }
}
