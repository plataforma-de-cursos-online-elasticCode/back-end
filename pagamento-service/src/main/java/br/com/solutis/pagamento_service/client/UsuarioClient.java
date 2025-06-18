package br.com.solutis.pagamento_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {
    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder builder){
        this.webClient = builder.baseUrl("localhost:8089/api/usuarios/").build(); // Gateway = roteamento
    }

    public Mono<UsuarioResponseDto> buscarUsuarioPorId(Long id){
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(UsuarioResponseDto.class);
    }
}
