package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 1/20/2018.
 */

public class Sponsors {
    private String name;
    private String photo;

    public Sponsors(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
