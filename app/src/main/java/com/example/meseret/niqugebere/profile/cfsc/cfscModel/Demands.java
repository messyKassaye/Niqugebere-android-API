package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by Meseret on 1/31/2018.
 */

public class Demands {
    private String id;
    private String company_name;
    private String company_photo;
    private String category_name;
    private String sub_category_name;
    private String title;
    private String product_photo;
    private String price;
    private String total_quantity;
    private String availability;
    private String description;

    public Demands() {
    }

    public Demands(String id, String company_name, String company_photo, String category_name, String sub_category_name, String title, String product_photo, String price, String total_quantity, String availability, String description) {
        this.id = id;
        this.company_name = company_name;
        this.company_photo = company_photo;
        this.category_name = category_name;
        this.sub_category_name = sub_category_name;
        this.title = title;
        this.product_photo = product_photo;
        this.price = price;
        this.total_quantity = total_quantity;
        this.availability = availability;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_photo() {
        return company_photo;
    }

    public void setCompany_photo(String company_photo) {
        this.company_photo = company_photo;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProduct_photo() {
        return product_photo;
    }

    public void setProduct_photo(String product_photo) {
        this.product_photo = product_photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
