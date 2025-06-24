package br.com.solutis.conteudo_service.mapper;

import br.com.solutis.conteudo_service.dto.ConteudoRequestDto;
import br.com.solutis.conteudo_service.entity.Conteudo;

public class ConteudoMapper {

    public static Conteudo toEntity(ConteudoRequestDto request) {
        Conteudo newConteudo = new Conteudo();

        newConteudo.setNome(request.nome());
        newConteudo.setVideoUrl(request.videoUrl());
        newConteudo.setFkCurso(request.fkCurso());

        return newConteudo;
    }

    public static Conteudo toConteudoAtualizado(Conteudo conteudoExistente, ConteudoRequestDto request) {
        conteudoExistente.setNome(request.nome());
        conteudoExistente.setVideoUrl(request.videoUrl());
        conteudoExistente.setFkCurso(request.fkCurso());

        return conteudoExistente;
    }
}
