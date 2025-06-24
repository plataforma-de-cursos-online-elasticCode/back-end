package br.com.solutis.gateway_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    private final List<String> ROTAS_PUBLICAS = List.of(
            "/api/usuarios/login",
            "/api/usuarios/cadastro"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod(); // tipo de requisição
        String path = exchange.getRequest().getURI().getPath(); // Path selecionado para direcionamento de microserviço

        if(ROTAS_PUBLICAS.stream().anyMatch(path::startsWith)){
            logger.info("Rota pública acessada");
            return chain.filter(exchange);
        }


        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Cabeçalho Authorization ausente ou inválido: {}", authHeader);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");

            if (roles == null || roles.isEmpty()) {
                logger.warn("Token JWT não contém roles");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // Lógica de autorização centralizada por rota + método + role
            if (!isAuthorized(path, method, roles)) {
                logger.warn("Acesso negado para a rota '{}', método '{}', roles {}", path, method, roles);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            logger.debug("Token JWT válido e autorizado para a rota '{}'", path);
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


    private boolean isAuthorized(String path, HttpMethod method, List<String> roles) {
        // Usuário com ROLE_PROFESSOR pode acessar tudo no /api/usuarios, /api/cursos e outros
        // Exemplos simplificados, ajustar conforme seu mapeamento real

        // Regras para microserviço de usuário
        if (path.startsWith("/api/usuarios")) {
            if (HttpMethod.POST.equals(method) && (path.endsWith("/login") || path.endsWith("/cadastro"))) {
                return true; // público
            }
            if (roles.contains("ROLE_PROFESSOR")) {
                return true; // PROFESSOR pode acessar todas rotas protegidas de usuário
            }
            return false;
        }

        // Regras para microserviço de curso
        if (path.startsWith("/api/cursos")) {
            // POST em /api/cursos (cadastrar) só PROFESSOR
            if (HttpMethod.POST.equals(method) && path.equals("/api/cursos")) {
                return roles.contains("ROLE_PROFESSOR");
            }

            // GET em /api/cursos, /api/cursos/{id}, /api/cursos/buscarPorNome para PROFESSOR e ALUNO
            if (HttpMethod.GET.equals(method)) {
                return roles.contains("ROLE_PROFESSOR") || roles.contains("ROLE_ALUNO");
            }

            // DELETE /api/cursos/deletar/{id} e PUT /api/cursos/atualizar/{id} só PROFESSOR
            if ((HttpMethod.DELETE.equals(method) && path.contains("/deletar/")) ||
                    (HttpMethod.PUT.equals(method) && path.contains("/atualizar/"))) {
                return roles.contains("ROLE_PROFESSOR");
            }

            // Por padrão bloqueia tudo que não casa acima
            return false;
        }

        if (path.startsWith("/api/conteudos")) {
            // POST em /api/conteudos (cadastrar) só PROFESSOR
            if (HttpMethod.POST.equals(method) && path.equals("/api/conteudos")) {
                return roles.contains("ROLE_PROFESSOR");
            }

            // GET em /api/conteudos (listar todos) e /api/conteudos/{id} (buscar por id) para PROFESSOR e ALUNO
            if (HttpMethod.GET.equals(method)) {
                return roles.contains("ROLE_PROFESSOR") || roles.contains("ROLE_ALUNO");
            }

            // DELETE em /api/conteudos/{id} só PROFESSOR
            if (HttpMethod.DELETE.equals(method) && path.matches("/api/conteudos/\\d+")) {
                return roles.contains("ROLE_PROFESSOR");
            }

            // PUT em /api/conteudos/{id} (atualizar) só PROFESSOR
            if (HttpMethod.PUT.equals(method) && path.matches("/api/conteudos/\\d+")) {
                return roles.contains("ROLE_PROFESSOR");
            }

            // Por padrão, bloqueia todas as outras rotas
            return false;
        }

        if (path.startsWith("/avaliacoes")) {
            // POST em /avaliacoes (atribuição de nota) só PROFESSOR
            if (HttpMethod.POST.equals(method) && path.equals("/avaliacoes")) {
                return roles.contains("ROLE_PROFESSOR");
            }

            // Outras rotas relacionadas à avaliação podem ser adicionadas aqui conforme necessário

            // Por padrão, bloquear todas as outras rotas
            return false;
        }

        // Se não tiver regra específica, exigir autenticação básica (token válido)
        return true;
    }
}