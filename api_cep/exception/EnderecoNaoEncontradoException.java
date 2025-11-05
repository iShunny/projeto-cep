package com.example.api_cep.exception;

public class EnderecoNaoEncontradoException extends RuntimeException {
    public EnderecoNaoEncontradoException(String message) {
        super(message);
    }
}
