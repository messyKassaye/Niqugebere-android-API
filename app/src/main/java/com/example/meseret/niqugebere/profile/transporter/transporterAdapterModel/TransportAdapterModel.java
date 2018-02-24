package com.example.meseret.niqugebere.profile.transporter.transporterAdapterModel;

/**
 * Created by Meseret on 2/18/2018.
 */

public class TransportAdapterModel {
    private int id;
    private String to_company;
    private String to_woreda;
    private String product_name;
    private String product_sub_name;
    private String title;
    private String plate_no;
    private String total_quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTo_company() {
        return to_company;
    }

    public void setTo_company(String to_company) {
        this.to_company = to_company;
    }

    public String getTo_woreda() {
        return to_woreda;
    }

    public void setTo_woreda(String to_woreda) {
        this.to_woreda = to_woreda;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_sub_name() {
        return product_sub_name;
    }

    public void setProduct_sub_name(String product_sub_name) {
        this.product_sub_name = product_sub_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }
}