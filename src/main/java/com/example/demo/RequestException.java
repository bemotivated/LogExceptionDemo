package com.example.demo;

public class RequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public RequestException(String message){
        super(message);
    }
}
