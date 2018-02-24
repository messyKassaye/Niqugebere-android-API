package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 1/25/2018.
 */

public class APISuccessResponse {

    private String status;
    private String token;

    public APISuccessResponse(String status, String token) {
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
