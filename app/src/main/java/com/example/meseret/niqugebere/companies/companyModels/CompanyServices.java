package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 1/24/2018.
 */

public class CompanyServices {
    private String id;
    private String name;
    private String photo_path;
    private String company_id;

    public CompanyServices() {
    }

    public CompanyServices(String id, String name, String photo_path, String company_id) {
        this.id = id;
        this.name = name;
        this.photo_path = photo_path;
        this.company_id = company_id;
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

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
