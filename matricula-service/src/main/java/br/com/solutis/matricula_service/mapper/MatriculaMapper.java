package br.com.solutis.matricula_service.mapper;

import br.com.solutis.matricula_service.entity.Matricula;
import org.springframework.stereotype.Component;

@Component
public class MatriculaMapper {

    // TODO: descomentar o método toResponse quando conseguir puxar o nome do aluno e curso futuramente.
//    public Matricula toResponse(Matricula entity) {
//        return (entity != null) ? Matricula.builder()
//                .id(entity.getId())
//                .aluno(entity.getAluno())
//                .curso(entity.getCurso())
//                .dataMatricula(entity.getDataMatricula())
//                .status(entity.getStatus())
//                .build() : null;
//    }

}
