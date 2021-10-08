package com.example.CrudApplicationUsingJpaMySql.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
@JsonInclude(JsonInclude.Include.NON_NULL) 	//  ignore all null fields
public class ApiException {

    private String message;
    private String description;
    private HttpStatus status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApiException(String message, String description, HttpStatus status) {
        this.message = message;
        this.description = description;
        this.status = status;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
