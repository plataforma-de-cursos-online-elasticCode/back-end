package br.com.solutis.usuario_service.service;

import io.jsonwebtoken.JwtException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final Key secretKey;

    public TokenService(@Value("${jwt.secret}") String secretKeyBase64){
        try {
            this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64));
            logger.info("Chave secreta carregada com sucesso no TokenService");
        } catch (Exception e) {
            logger.error("Erro ao decodificar a chave secreta: {}", e.getMessage());
            throw new IllegalArgumentException("Chave secreta inválida. Verifique a configuração em jwt.secret", e);
        }
    }

    public String gerarToken(String username) {
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
    }

    public String extrairUsername(String token) {
        try {
            return io.jsonwebtoken.Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (io.jsonwebtoken.JwtException e) {
            logger.warn("Falha ao extrair username do token: {}", e.getMessage());
            throw new IllegalArgumentException("Token inválido", e);
        }
    }

    public boolean validarToken(String token) {
        try {
            io.jsonwebtoken.Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            logger.debug("Token validado com sucesso: {}", token);
            return true;
        } catch (JwtException e) {
            logger.warn("Falha na validação do token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Erro inesperado ao validar token: {}", e.getMessage());
            return false;
        }
    }
}
