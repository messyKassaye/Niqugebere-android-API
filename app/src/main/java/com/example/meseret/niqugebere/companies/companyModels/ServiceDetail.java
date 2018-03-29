package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by meseret on 3/3/18.
 */

public class ServiceDetail {
    private String title;
    private String description;

    public ServiceDetail() {
    }

    public ServiceDetail(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
