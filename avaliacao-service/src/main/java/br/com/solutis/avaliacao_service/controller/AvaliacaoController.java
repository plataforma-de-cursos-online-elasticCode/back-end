package br.com.solutis.avaliacao_service.controller;

import br.com.solutis.avaliacao_service.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    private final WebClient cursoWebClient;
    private final WebClient usuarioWebClient;

    @Autowired
    public AvaliacaoController(WebClient.Builder webClientBuilder) {
        this.cursoWebClient = webClientBuilder.baseUrl("http://localhost:8085").build();
        this.usuarioWebClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

}
