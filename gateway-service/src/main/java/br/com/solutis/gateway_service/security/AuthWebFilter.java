package br.com.solutis.gateway_service.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Base64;
import java.util.List;

@Component
public class AuthWebFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthWebFilter.class);

    private final Key secretKey;

    // Injeta a chave do application.properties ou variável de ambiente
    public AuthWebFilter(@Value("${jwt.secret}") String secretKeyBase64) {
        try {
            this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64));
        } catch (Exception e) {
            logger.error("Erro ao decodificar a chave secreta: {}", e.getMessage());
            throw new IllegalArgumentException("Chave secreta inválida. Verifique a configuração em jwt.secret", e);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final List<String> ROTAS_PUBLICAS = List.of(
                "/api/usuarios/login",
                "/api/usuarios/cadastro"
        );

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String path = exchange.getRequest().getURI().getPath();

        if(ROTAS_PUBLICAS.stream().anyMatch(path::startsWith)){
            logger.info("Rota pública acessada");
            return chain.filter(exchange);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Cabeçalho Authorization ausente ou inválido: {}", authHeader);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            logger.debug("Token JWT válido para a requisição: {}", exchange.getRequest().getURI());
            return chain.filter(exchange);
        } catch (JwtException e) {
            logger.warn("Falha na validação do token JWT: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            logger.error("Erro inesperado ao validar token: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}