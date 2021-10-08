package com.example.CrudApplicationUsingJpaMySql.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class EmailRequest {
    @NotEmpty(message = "Email address cannot be null")
    @Email(message = "This email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    public EmailRequest(String email) {
        this.email = email;
    }
    public EmailRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
