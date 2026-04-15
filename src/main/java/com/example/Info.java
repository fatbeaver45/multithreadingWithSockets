package com.example;

import java.io.Serializable;

public class Info implements Serializable {
    private String message;

    public Info(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}