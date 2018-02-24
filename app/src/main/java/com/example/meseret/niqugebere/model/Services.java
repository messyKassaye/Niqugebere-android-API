package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 1/27/2018.
 */

public class Services {
    private String id;
    private String name;
    private String photo_path;

    public Services(String id, String name, String photo_path) {
        this.id = id;
        this.name = name;
        this.photo_path = photo_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }
}
