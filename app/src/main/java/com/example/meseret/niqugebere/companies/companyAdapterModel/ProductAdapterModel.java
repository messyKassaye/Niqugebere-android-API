package com.example.meseret.niqugebere.companies.companyAdapterModel;

/**
 * Created by Meseret on 1/23/2018.
 */

public class ProductAdapterModel {
    private String id;
    private String name;
    private String photo;
    private String product_id;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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
