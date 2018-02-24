package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 2/14/2018.
 */

public class Pull {
    private String id;
    private String company_name;
    private String product_name;
    private String total_quantity;
    private String product_photo;
    private String woreda;
    private String special_name;
    private String category_name;
    private String sub_category_name;
    private String company_photo;
    private String price;

    public Pull(String id, String company_name, String product_name, String total_quantity, String product_photo, String woreda, String special_name, String category_name, String sub_category_name, String company_photo, String price) {
        this.id = id;
        this.company_name = company_name;
        this.product_name = product_name;
        this.total_quantity = total_quantity;
        this.product_photo = product_photo;
        this.woreda = woreda;
        this.special_name = special_name;
        this.category_name = category_name;
        this.sub_category_name = sub_category_name;
        this.company_photo = company_photo;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompany_photo() {
        return company_photo;
    }

    public void setCompany_photo(String company_photo) {
        this.company_photo = company_photo;
    }

    public String getProduct_photo() {
        return product_photo;
    }

    public void setProduct_photo(String product_photo) {
        this.product_photo = product_photo;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getWoreda() {
        return woreda;
    }

    public void setWoreda(String woreda) {
        this.woreda = woreda;
    }

    public String getSpecial_name() {
        return special_name;
    }

    public void setSpecial_name(String special_name) {
        this.special_name = special_name;
    }
}
