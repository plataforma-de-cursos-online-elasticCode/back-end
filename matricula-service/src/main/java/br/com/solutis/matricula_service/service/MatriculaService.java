package br.com.solutis.matricula_service.service;

import br.com.solutis.matricula_service.dto.MatriculaRequestDto;
import br.com.solutis.matricula_service.entity.Matricula;
import br.com.solutis.matricula_service.exception.AlunoJaMatriculadoException;
import br.com.solutis.matricula_service.mapper.MatriculaMapper;
import br.com.solutis.matricula_service.repository.MatriculaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository repository;

    @Autowired
    private RabbitTemplate template;

    MatriculaMapper mapper;

    public List<Matricula> listarMatriculas() {
        return repository.findAll();
    }

    public Optional<Matricula> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Matricula matricular(MatriculaRequestDto req) {
        if (repository.existsByAlunoIdAndCursoId(req.getAlunoId(), req.getCursoId())) {
            throw new AlunoJaMatriculadoException("Este aluno já está matriculado neste curso.");
        }

        Matricula matricula = mapper.toEntity(req);
        repository.save(matricula);
        template.convertAndSend("matricula.nova", matricula);
        return matricula;
    }

    public List<Matricula> listarMatriculasPorAluno(Long id) {
        if (id == null) return null;

        return repository.findAllByAlunoId(id);
    }

    public List<Matricula> listarMatriculasPorCursoId(Long id) {
        if (id == null) return null;

        return repository.findAllByCursoId(id);
    }

}
