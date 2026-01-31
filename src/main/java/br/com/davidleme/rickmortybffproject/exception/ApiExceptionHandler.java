package br.com.davidleme.rickmortybffproject.exception;

import br.com.davidleme.rickmortybffproject.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PersonagemNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePersonagemNaoEncontrado(PersonagemNaoEncontradoException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}