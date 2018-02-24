package com.example.meseret.niqugebere.profile.cfsc.cfscModel;

/**
 * Created by Meseret on 2/1/2018.
 */

public class CFSCInvetory {
    private String sub_category_id;
    private String product_name;
    private String quantity;

    public CFSCInvetory(String sub_category_id, String product_name, String quantity) {
        this.sub_category_id = sub_category_id;
        this.product_name = product_name;
        this.quantity = quantity;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
