package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 1/28/2018.
 */

public class MarketLists {
    private String id;
    private String name;

    public MarketLists(String id, String name) {
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
