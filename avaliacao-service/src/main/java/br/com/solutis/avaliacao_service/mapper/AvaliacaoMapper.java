package br.com.solutis.avaliacao_service.mapper;

import br.com.solutis.avaliacao_service.dto.AvaliacaoRequestDto;
import br.com.solutis.avaliacao_service.dto.AvaliacaoResponseDto;
import br.com.solutis.avaliacao_service.entity.Avaliacao;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoMapper {

    public Avaliacao toEntity(AvaliacaoRequestDto req) {
        if (req == null) {
            return null;
        }
        return Avaliacao.builder()
                .alunoId(req.getAlunoId())
                .cursoId(req.getCursoId())
                .nota(req.getNota())
                .comentario(req.getComentario())
                .build();
    }

    public AvaliacaoResponseDto toResponse(Avaliacao entity) {
        if (entity == null) {
            return null;
        }
        return AvaliacaoResponseDto.builder()
                .nomeAluno(null)
                .nomeCurso(null)
                .nota(entity.getNota())
                .comentario(entity.getComentario())
                .build();
    }

}
