package br.com.solutis.pagamento_service.client;

import br.com.solutis.pagamento_service.exception.EntidadeNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component // Componente para ser usado pelo Spring Boot
public class CursoClient {
    private final WebClient webClient; // Objeto de consultas reativas

    public CursoClient(WebClient.Builder builder){
        this.webClient = builder.baseUrl("http://localhost:8085").build(); // Acionando objeto personalizado (builder)
    }

    public Mono<CursoResponseDto> buscarCursoPorId(Long id, String token){ // Parâmetro de consulta esperado
        return webClient.get() // Metodo HTTP acionado
                .uri("/{id}", id) // URI para especificar endpoint
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if(response.statusCode().equals(HttpStatus.NOT_FOUND)){
                        return Mono.error(new EntidadeNaoEncontradaException("Curso com id %d não encontrado".formatted(id)));
                    } else{
                        return Mono.error(new RuntimeException("Erro ao buscar curso"));
                    }
                })
                .bodyToMono(CursoResponseDto.class);
    }
}
