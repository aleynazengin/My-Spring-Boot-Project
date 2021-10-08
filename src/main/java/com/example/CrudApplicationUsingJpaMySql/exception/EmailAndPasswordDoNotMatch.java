package com.example.CrudApplicationUsingJpaMySql.exception;

import org.springframework.http.HttpStatus;

public class EmailAndPasswordDoNotMatch extends Exception {

    public EmailAndPasswordDoNotMatch(String message) {
        super(message);
    }

    public EmailAndPasswordDoNotMatch(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAndPasswordDoNotMatch() {
        super();
    }

}
