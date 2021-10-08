package com.example.CrudApplicationUsingJpaMySql.exception;

public class UserNotActivatedException extends Exception{
    public UserNotActivatedException() {
        super();
    }

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
