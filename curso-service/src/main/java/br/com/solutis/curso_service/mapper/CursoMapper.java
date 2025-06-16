package br.com.solutis.curso_service.mapper;

import br.com.solutis.curso_service.dto.CursoRequestDto;
import br.com.solutis.curso_service.dto.CursoResponseDto;
import br.com.solutis.curso_service.entity.Curso;

public class CursoMapper {

    public static Curso toEntity(CursoRequestDto request) {
        Curso newCurso = new Curso();

        newCurso.setNome(request.nome());
        newCurso.setDescricao(request.descricao());
        newCurso.setPreco(request.preco());
        newCurso.setCategoria(request.categoria());

        return newCurso;
    }

    public static CursoResponseDto toResponse(Curso entity) {

        return new CursoResponseDto(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.getCategoria()
        );
    }

    public static Curso updateCurso(Curso cursoExistente, CursoRequestDto updateCurso) {

        cursoExistente.setNome(updateCurso.nome());
        cursoExistente.setDescricao(updateCurso.descricao());
        cursoExistente.setPreco(updateCurso.preco());
        cursoExistente.setCategoria(updateCurso.categoria());

        return cursoExistente;
    }
}
