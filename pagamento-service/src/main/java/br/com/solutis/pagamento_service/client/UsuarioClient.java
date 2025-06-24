package br.com.solutis.pagamento_service.client;

import br.com.solutis.pagamento_service.exception.EntidadeNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if(response.statusCode().equals(HttpStatus.NOT_FOUND)){
                        return Mono.error(new EntidadeNaoEncontradaException("Usuário com id %d não encontrado".formatted(id)));
                    } else{
                        return Mono.error(new RuntimeException("Erro ao buscar usuário"));
                    }
                })
                .bodyToMono(UsuarioResponseDto.class);
    }
}
