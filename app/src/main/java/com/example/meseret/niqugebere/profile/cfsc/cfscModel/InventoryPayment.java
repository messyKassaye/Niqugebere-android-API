package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by Meseret on 2/1/2018.
 */

public class InventoryPayment {
    private String name;
    private String photo_path;
    private String total_payment;

    public InventoryPayment(String name, String photo_path, String total_payment) {
        this.name = name;
        this.photo_path = photo_path;
        this.total_payment = total_payment;
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

    public String getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(String total_payment) {
        this.total_payment = total_payment;
    }
}
