package com.example.meseret.niqugebere.model;

/**
 * Created by meseret on 2/24/18.
 */

public class ProductCategory {
    private int id;
    private String name;

    public ProductCategory() {
    }

    public ProductCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
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
