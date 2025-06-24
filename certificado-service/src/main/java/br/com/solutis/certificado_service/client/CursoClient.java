package br.com.solutis.certificado_service.client;

import br.com.solutis.certificado_service.dto.cursodto.CursoResponseDto;
import br.com.solutis.certificado_service.exception.CursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CursoClient {

    @Value("${curso.service.url}")
    private String cursoServiceUrl;

    private final WebClient webClient;

    public CursoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public CursoResponseDto buscarCursoPorId(Long id) {
        return webClient.get()
                .uri(cursoServiceUrl + "/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new CursoNaoEncontradoException("Curso com ID " + id + " não encontrado"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Erro interno no curso-service"))
                )
                .bodyToMono(CursoResponseDto.class)
                .block();
    }

}
