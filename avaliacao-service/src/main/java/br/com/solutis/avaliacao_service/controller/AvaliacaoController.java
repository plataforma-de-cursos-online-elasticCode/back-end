package br.com.solutis.avaliacao_service.controller;

import br.com.solutis.avaliacao_service.dto.AvaliacaoRequestDto;
import br.com.solutis.avaliacao_service.dto.AvaliacaoResponseDto;
import br.com.solutis.avaliacao_service.mapper.AvaliacaoMapper;
import br.com.solutis.avaliacao_service.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    private final WebClient cursoWebClient;
    private final WebClient usuarioWebClient;

    AvaliacaoMapper mapper;

    @Autowired
    public AvaliacaoController(WebClient.Builder webClientBuilder) {
        this.cursoWebClient = webClientBuilder.baseUrl("http://localhost:8085").build();
        this.usuarioWebClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDto> atribuirNota(@RequestBody AvaliacaoRequestDto req,
                                                             @RequestHeader("Authorization") String authorizationHeader) {
        AvaliacaoResponseDto response = mapper.toResponse(avaliacaoService.atribuirNota(req));

        String urlCurso = "/" + req.getCursoId();
        String nomeCurso = cursoWebClient.get()
                .uri(urlCurso)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String urlUsuario = "/" + req.getAlunoId();
        String nomeAluno = usuarioWebClient.get()
                .uri(urlUsuario)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        response.setNomeCurso(nomeCurso);
        response.setNomeAluno(nomeAluno);

        return ResponseEntity.ok(response);
    }

}
