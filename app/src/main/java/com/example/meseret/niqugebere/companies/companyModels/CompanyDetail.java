package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 1/21/2018.
 */

public class CompanyDetail {
    private String name;
    private String photo;

    public CompanyDetail(String name, String photo) {
        this.name = name;
        this.photo = photo;
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
