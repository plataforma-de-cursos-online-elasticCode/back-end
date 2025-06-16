package br.com.solutis.matricula_service.service;

import br.com.solutis.matricula_service.dto.MatriculaRequestDto;
import br.com.solutis.matricula_service.entity.Matricula;
import br.com.solutis.matricula_service.exception.AlunoJaMatriculadoException;
import br.com.solutis.matricula_service.mapper.MatriculaMapper;
import br.com.solutis.matricula_service.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository repository;

    MatriculaMapper mapper;

    public List<Matricula> listarMatriculas() {
        return repository.findAll();
    }

    public Optional<Matricula> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Matricula matricular(MatriculaRequestDto req) {
        if (repository.existsByAlunoIdAndCursoId(req.alunoId(), req.cursoId())) {
            throw new AlunoJaMatriculadoException("Este aluno já está matriculado neste curso.");
        }

        return repository.save(mapper.toEntity(req));
    }

}
