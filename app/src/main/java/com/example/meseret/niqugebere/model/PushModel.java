package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 12/8/2017.
 */

public class PushModel {
    private String woreda_id;
    private String product_id;
    private String product_category_id;
    private String quantity;
    private String delivery_time;
    private String description;
    private String phone_number;

    public PushModel(String woreda_id, String product_id, String product_category_id, String quantity, String delivery_time, String description, String phone_number) {
        this.woreda_id = woreda_id;
        this.product_id = product_id;
        this.product_category_id = product_category_id;
        this.quantity = quantity;
        this.delivery_time = delivery_time;
        this.description = description;
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWoreda_id() {
        return woreda_id;
    }

    public void setWoreda_id(String woreda_id) {
        this.woreda_id = woreda_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(String product_category_id) {
        this.product_category_id = product_category_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
