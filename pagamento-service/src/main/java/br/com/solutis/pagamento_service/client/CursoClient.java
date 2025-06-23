package br.com.solutis.pagamento_service.client;

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
                .bodyToMono(CursoResponseDto.class);
    }
}
