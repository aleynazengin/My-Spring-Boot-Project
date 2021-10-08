package com.example.CrudApplicationUsingJpaMySql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class AuthResponse {

    private final String token;

    public AuthResponse(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
