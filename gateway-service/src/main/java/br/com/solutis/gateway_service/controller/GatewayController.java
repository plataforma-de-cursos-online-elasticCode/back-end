package br.com.solutis.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final WebClient webClient;

    public GatewayController() {
        this.webClient = WebClient.builder().build();
    }

    // As chaves no RequestMapping da duas opções (sobrecarga), uma com URI e outra com path vazio
    @RequestMapping({"/{service}" ,"/{service}/{path:^(?!api).*$}/**"})
    public Mono<ResponseEntity<String>> proxy(
            @PathVariable String service,
            @PathVariable(required = false) String path,
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) MultiValueMap<String, String> queryParams,
            @RequestBody(required = false) Mono<String> body,
            ServerHttpRequest request
    ) {
        String baseUrl = switch (service) {
            case "certificados" -> "http://localhost:8088";
            case "matriculas" -> "http://localhost:8083";
            case "pagamentos" -> "http://localhost:8082";
            case "usuarios" -> "http://localhost:8081";
            case "cursos" -> "http://localhost:8085";
            case "conteudos" -> "http://localhost:8086";
            case "avaliacoes" -> "http://localhost:8087";
            default -> null;
        };

        System.out.println("URL recebida: " + baseUrl);

        if (baseUrl == null) return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Serviço não encontrado"));

        String fullPath = request.getURI().getRawPath().replace("/api/" + service, "");

        return webClient.method(request.getMethod())
                .uri(baseUrl + fullPath)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(body == null ? Mono.empty() : body, String.class)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    return response.bodyToMono(String.class) // Le o corpo da resposta do microserviço como String, de forma assíncrona (Mono = reativo)
                            .defaultIfEmpty("") // Se não possuir um corpo na resposta (como um 204), retorna vazio
                            .map(responseBody -> { // Quando mono estiver pronto mapeia ele para um ResponseEntity
                                HttpHeaders responseHeaders = new HttpHeaders();
                                response.headers().asHttpHeaders().forEach(responseHeaders::put); // Capturando headers e inserindo em uma classe especializada

                                // Montagem padrão de resposta com ResponseEntity, com os dados já extraídos do microserviço
                                return ResponseEntity.status(status)
                                        .headers(responseHeaders)
                                        .body(responseBody);
                            });
                });
    }
}