package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 1/25/2018.
 */

public class PostedProducts {
    private String id;
    private String product_name;
    private String unit;
    private String unit_price;
    private String quantity;
    private String product_photo;
    private String sub_category_name;

    public PostedProducts(String id, String product_name, String unit, String unit_price, String quantity, String product_photo, String sub_category_name) {
        this.id = id;
        this.product_name = product_name;
        this.unit = unit;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.product_photo = product_photo;
        this.sub_category_name = sub_category_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_photo() {
        return product_photo;
    }

    public void setProduct_photo(String product_photo) {
        this.product_photo = product_photo;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }
}
