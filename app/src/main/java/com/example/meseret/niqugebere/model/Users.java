package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 11/26/2017.
 */

public class Users {
    private String first_name;
    private String last_name;
    private String tin_no;
    private String phone_number;
    private String categor_id;
    private String company_name;
    private String woreda_id;
    private String special_name;
    private String password;

    public Users(String first_name, String last_name, String tin_no, String phone_number, String categor_id, String company_name, String woreda_id, String special_name, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.tin_no = tin_no;
        this.phone_number = phone_number;
        this.categor_id = categor_id;
        this.company_name = company_name;
        this.woreda_id = woreda_id;
        this.special_name = special_name;
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTin_no() {
        return tin_no;
    }

    public void setTin_no(String tin_no) {
        this.tin_no = tin_no;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCategor_id() {
        return categor_id;
    }

    public void setCategor_id(String categor_id) {
        this.categor_id = categor_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getWoreda_id() {
        return woreda_id;
    }

    public void setWoreda_id(String woreda_id) {
        this.woreda_id = woreda_id;
    }

    public String getSpecial_name() {
        return special_name;
    }

    public void setSpecial_name(String special_name) {
        this.special_name = special_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
