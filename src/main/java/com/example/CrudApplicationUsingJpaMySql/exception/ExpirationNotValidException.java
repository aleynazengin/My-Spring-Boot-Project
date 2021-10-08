package com.example.CrudApplicationUsingJpaMySql.exception;

public class ExpirationNotValidException extends Exception{
    public ExpirationNotValidException() {
        super();
    }

    public ExpirationNotValidException(String message) {
        super(message);
    }

    public ExpirationNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
