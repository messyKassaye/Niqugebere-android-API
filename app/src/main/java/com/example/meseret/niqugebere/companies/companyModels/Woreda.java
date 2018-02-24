package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 11/28/2017.
 */

public class Woreda {
    private int id;
    private String name;

    public Woreda() {
    }

    public Woreda(int id, String name) {
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
