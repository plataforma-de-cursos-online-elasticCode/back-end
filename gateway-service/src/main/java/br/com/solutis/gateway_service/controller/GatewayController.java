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
            // REMOVIDO!  @PathVariable String path,
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
            default -> null;
        };

        System.out.println("URL recebida: " + baseUrl);

        if (baseUrl == null) return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Serviço não encontrado"));

        String fullPath = request.getURI().getRawPath().replace("/api/" + service, "");

        return webClient.method(request.getMethod())
                .uri(baseUrl + fullPath)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(body == null ? Mono.empty() : body, String.class)
                .retrieve()
                .toEntity(String.class);
    }
}