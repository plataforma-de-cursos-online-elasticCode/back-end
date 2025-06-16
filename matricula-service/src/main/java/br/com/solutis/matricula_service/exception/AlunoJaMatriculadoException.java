package br.com.solutis.matricula_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlunoJaMatriculadoException extends RuntimeException {
    public AlunoJaMatriculadoException(String message) {
        super(message);
    }
}
