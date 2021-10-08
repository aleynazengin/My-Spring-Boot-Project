package com.example.CrudApplicationUsingJpaMySql.exception;

public class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException() {
        super();
    }


    public EmailAlreadyExistsException(String message) {
        super(message);
    }


    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
