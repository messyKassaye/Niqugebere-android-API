package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 11/27/2017.
 */

public class ResponseToken {
    private String status;
    private String token;

    public ResponseToken(String status, String token) {
        this.status = status;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
