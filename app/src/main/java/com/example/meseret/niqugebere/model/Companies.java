package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 2/3/2018.
 */

public class Companies {
    private String id;
    private String company_name;
   private String owner_name;
    private String phone;

    public Companies(String id, String company_name, String owner_name, String phone) {
        this.id = id;
        this.company_name = company_name;
        this.owner_name = owner_name;
        this.phone = phone;
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

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
