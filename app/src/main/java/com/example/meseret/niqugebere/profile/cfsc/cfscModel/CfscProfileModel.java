package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by Meseret on 1/26/2018.
 */

public class CfscProfileModel {
    private String name;
    private String logo;
    private String description;

    public CfscProfileModel(String name, String logo, String description) {
        this.name = name;
        this.logo = logo;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
