package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 1/25/2018.
 */

public class Order {
    private String company_id;
    private String post_product_id;
    private String full_name;
    private String phone_no;
    private String total_quantity;
    private String woreda_id;

    public Order(String company_id, String post_product_id, String full_name, String phone_no, String total_quantity, String woreda_id) {
        this.company_id = company_id;
        this.post_product_id = post_product_id;
        this.full_name = full_name;
        this.phone_no = phone_no;
        this.total_quantity = total_quantity;
        this.woreda_id = woreda_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getPost_product_id() {
        return post_product_id;
    }

    public void setPost_product_id(String post_product_id) {
        this.post_product_id = post_product_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getWoreda_id() {
        return woreda_id;
    }

    public void setWoreda_id(String woreda_id) {
        this.woreda_id = woreda_id;
    }
}
