package br.com.solutis.certificado_service.client;

import br.com.solutis.certificado_service.dto.usuariodto.UsuarioResponseDto;
import br.com.solutis.certificado_service.exception.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UsuarioClient {

    @Value("${usuario.service.url}")
    private String usuarioServiceUrl;

    private final WebClient webClient;

    public UsuarioClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public UsuarioResponseDto buscarUsuarioPorId(Long id, String token) {
        return webClient.get()
                .uri(usuarioServiceUrl + "/{id}", id)
                .headers(headers -> headers.set("Authorization", token))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Erro interno no usuário-service"))
                )
                .bodyToMono(UsuarioResponseDto.class)
                .block();
    }


}
