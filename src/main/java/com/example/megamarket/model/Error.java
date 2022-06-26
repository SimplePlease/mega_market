package com.example.megamarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty
    private int code;
    @JsonProperty
    private String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
