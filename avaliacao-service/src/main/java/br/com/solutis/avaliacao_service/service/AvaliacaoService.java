package br.com.solutis.avaliacao_service.service;

import br.com.solutis.avaliacao_service.dto.AvaliacaoRequestDto;
import br.com.solutis.avaliacao_service.entity.Avaliacao;
import br.com.solutis.avaliacao_service.mapper.AvaliacaoMapper;
import br.com.solutis.avaliacao_service.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository repository;

    AvaliacaoMapper mapper;

    public Avaliacao atribuirNota(AvaliacaoRequestDto req) {
        if (req == null || req.getAlunoId() == null || req.getCursoId() == null) {
            throw new IllegalArgumentException("A avaliação deve conter alunoId e cursoId.");
        }

        return repository.save(mapper.toEntity(req));
    }

}
