package com.example.CrudApplicationUsingJpaMySql.annotation;

public enum ValidationType {
    ANNONYMOUS ("Online");

    String value;

    ValidationType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
