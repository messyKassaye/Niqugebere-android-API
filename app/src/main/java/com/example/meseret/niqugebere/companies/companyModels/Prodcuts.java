package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 1/23/2018.
 */

public class Prodcuts {
    private String id;
    private String name;
    private String photo;
    private String category_name;
    private String price;

    public Prodcuts(String id, String name, String photo, String category_name, String price) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.category_name = category_name;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
