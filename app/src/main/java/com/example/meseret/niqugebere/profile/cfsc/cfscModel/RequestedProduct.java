package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by Meseret on 2/1/2018.
 */

public class RequestedProduct {
    private String product_photo;
    private String product_name;

    public RequestedProduct(String product_photo, String product_name) {
        this.product_photo = product_photo;
        this.product_name = product_name;
    }

    public String getProduct_photo() {
        return product_photo;
    }

    public void setProduct_photo(String product_photo) {
        this.product_photo = product_photo;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
