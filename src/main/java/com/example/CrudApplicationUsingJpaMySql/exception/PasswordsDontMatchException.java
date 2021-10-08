package com.example.CrudApplicationUsingJpaMySql.exception;

public class PasswordsDontMatchException extends Exception{
    public PasswordsDontMatchException() {
        super();
    }

    public PasswordsDontMatchException(String message) {
        super(message);
    }

    public PasswordsDontMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
