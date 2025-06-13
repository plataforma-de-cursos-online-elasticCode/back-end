package br.com.solutis.curso_service.service;

import br.com.solutis.curso_service.dto.CursoRequestDto;
import br.com.solutis.curso_service.dto.CursoResponseDto;
import br.com.solutis.curso_service.entity.Curso;
import br.com.solutis.curso_service.exception.CursoNaoEncontradoException;
import br.com.solutis.curso_service.mapper.CursoMapper;
import br.com.solutis.curso_service.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository repository;

    public CursoResponseDto cadastrarCurso(CursoRequestDto request) {
        Curso newCurso =  CursoMapper.toEntity(request);

        repository.save(newCurso);

        return CursoMapper.toResponse(newCurso);
    }

    public List<CursoResponseDto> listarTodosCursos() {
        return repository.findAll().stream().map(CursoMapper::toResponse).collect(Collectors.toList());
    }

    public CursoResponseDto buscarCursoPorId(Long id) {
        Optional<CursoResponseDto> response = repository.findById(id).map(CursoMapper::toResponse);

        return response.orElseThrow(() -> new CursoNaoEncontradoException("Curso de id %d não encontrado".formatted(id)));
    }

    public CursoResponseDto buscarCursoPorNome(String nome) {
        Curso cursoPorNome = repository.findByNome(nome);

        Optional<CursoResponseDto> response = Optional.of(CursoMapper.toResponse(cursoPorNome));

        return response.orElseThrow(() -> new CursoNaoEncontradoException("Curso de nome %s não encontrado".formatted(nome)));
    }

    public void deletarCursoPorId(Long id) {
        Optional<Curso> curso = repository.findById(id);

        if (curso.isPresent()) {
            repository.deleteById(id);
        }

        curso.orElseThrow(() -> new CursoNaoEncontradoException("Curso de id %d não encontrado".formatted(id)));
    }

}
