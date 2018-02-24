package com.example.meseret.niqugebere.adaptermodel;

/**
 * Created by Meseret on 1/28/2018.
 */

public class MarketsModelAdapter {
    private int id;
    private String name;
    private String photo_path;

    public int getId() {
        return id;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
