package com.example.CrudApplicationUsingJpaMySql.exception;

public class UsernameAlreadyExistsException extends Exception{
    public UsernameAlreadyExistsException() {
    }

    public UsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
