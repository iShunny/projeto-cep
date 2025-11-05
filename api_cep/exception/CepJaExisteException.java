package com.example.api_cep.exception;

public class CepJaExisteException extends RuntimeException {
    public CepJaExisteException(String message) {
        super(message);
    }
}
