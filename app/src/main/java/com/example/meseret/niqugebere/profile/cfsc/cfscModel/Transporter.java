package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by Meseret on 2/16/2018.
 */

public class Transporter {
    private String id;
    private String company_name;
    private String plate_no;
    private String capacity;
    private String company_photo;

    public Transporter(String id, String company_name, String plate_no, String capacity, String company_photo) {
        this.id = id;
        this.company_name = company_name;
        this.plate_no = plate_no;
        this.capacity = capacity;
        this.company_photo = company_photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_photo() {
        return company_photo;
    }

    public void setCompany_photo(String company_photo) {
        this.company_photo = company_photo;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
