package br.com.davidleme.rickmortybffproject.exception;

public class PersonagemNaoEncontradoException extends RuntimeException {

    public PersonagemNaoEncontradoException() {
        super("Personagem não encontrado");
    }
}