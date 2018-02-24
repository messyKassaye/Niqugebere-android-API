package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by Meseret on 1/30/2018.
 */

public class CFSCProductSubCategory {
    private int id;
    private String name;

    public CFSCProductSubCategory(int id, String name) {
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
