package br.com.solutis.matricula_service.mapper;

import br.com.solutis.matricula_service.dto.MatriculaAlunoResponseDto;
import br.com.solutis.matricula_service.dto.MatriculaCursoResponseDto;
import br.com.solutis.matricula_service.dto.MatriculaRequestDto;
import br.com.solutis.matricula_service.dto.MatriculaResponseDto;
import br.com.solutis.matricula_service.entity.Matricula;
import br.com.solutis.matricula_service.entity.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MatriculaMapper {

    public MatriculaResponseDto toResponse(Matricula entity) {
        return (entity != null) ? new MatriculaResponseDto(
                entity.getId(),
                null,
                null,
                entity.getStatus(),
                entity.getDataMatricula()
        ) : null;
    }

    public Matricula toEntity(MatriculaRequestDto req) {
        return (req != null) ? new Matricula(
                null,
                LocalDate.now(),
                Status.ATIVA,
                req.alunoId(),
                req.cursoId()
        ) : null;
    }

    public MatriculaAlunoResponseDto toAlunoResponse(Matricula entity) {
        return (entity != null) ? new MatriculaAlunoResponseDto(
                entity.getId(),
                null,
                entity.getStatus(),
                entity.getDataMatricula()
        ) : null;
    }

    public MatriculaCursoResponseDto toCursoResponse(Matricula entity) {
        return (entity != null) ? new MatriculaCursoResponseDto(
                entity.getId(),
                null,
                entity.getStatus(),
                entity.getDataMatricula()
        ) : null;
    }

}
