package com.example.CrudApplicationUsingJpaMySql.exception;

public class CodeDoesNotMatchException extends Exception{
    public CodeDoesNotMatchException() {
        super();
    }

    public CodeDoesNotMatchException(String message) {
        super(message);
    }

    public CodeDoesNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
