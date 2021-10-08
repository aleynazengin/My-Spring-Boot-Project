package com.example.CrudApplicationUsingJpaMySql.exception;

public class EmailDoesNotExistException extends Exception{
    public EmailDoesNotExistException() {
        super();
    }

    public EmailDoesNotExistException(String message) {
        super(message);
    }

    public EmailDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
