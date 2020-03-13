package com.example.demo.Exception;

public class JacksonException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public JacksonException(String message) {
        super(message);
    }
}

