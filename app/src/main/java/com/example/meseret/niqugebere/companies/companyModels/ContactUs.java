package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 1/23/2018.
 */

public class ContactUs {
    private String region;
    private String zone;
    private String woreda;
    private String special_name;
    private String phone;
    private String lan;
    private String lat;

    public ContactUs(String region, String zone, String woreda, String special_name, String phone, String lan, String lat) {
        this.region = region;
        this.zone = zone;
        this.woreda = woreda;
        this.special_name = special_name;
        this.phone = phone;
        this.lan = lan;
        this.lat = lat;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
