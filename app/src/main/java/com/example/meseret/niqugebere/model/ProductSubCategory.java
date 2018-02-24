package com.example.meseret.niqugebere.model;

/**
 * Created by meseret on 2/24/18.
 */

public class ProductSubCategory {
    private String id;
    private String name;

    public ProductSubCategory() {
    }

    public ProductSubCategory(String id, String name) {
        this.id = id;
        this.name = name;
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
}
