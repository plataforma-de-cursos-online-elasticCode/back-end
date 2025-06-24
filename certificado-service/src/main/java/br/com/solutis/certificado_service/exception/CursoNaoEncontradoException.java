package br.com.solutis.certificado_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CursoNaoEncontradoException extends RuntimeException {
    public CursoNaoEncontradoException(String message) {
        super(message);
    }
}
