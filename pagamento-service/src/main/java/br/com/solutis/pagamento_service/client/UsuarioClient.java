package br.com.solutis.pagamento_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {
    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder builder){
        this.webClient = builder.baseUrl("http://localhost:8081").build(); // Gateway = roteamento
    }

    public Mono<UsuarioResponseDto> buscarUsuarioPorId(Long id, String token){
        return webClient.get()
                .uri("/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(UsuarioResponseDto.class);
    }
}
