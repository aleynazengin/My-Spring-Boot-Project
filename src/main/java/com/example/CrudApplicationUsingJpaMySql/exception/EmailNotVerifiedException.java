package com.example.CrudApplicationUsingJpaMySql.exception;

public class EmailNotVerifiedException extends Exception{
    public EmailNotVerifiedException() {
        super();
    }

    public EmailNotVerifiedException(String message) {
        super(message);
    }

    public EmailNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
