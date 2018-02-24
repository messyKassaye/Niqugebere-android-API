package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 11/30/2017.
 */

public class LoginUser {
    private String tin;
    private String password;

    public LoginUser(String tin, String password) {
        this.tin = tin;
        this.password = password;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
