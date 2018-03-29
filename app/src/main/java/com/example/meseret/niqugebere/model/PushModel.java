package com.example.meseret.niqugebere.model;

/**
 * Created by Meseret on 12/8/2017.
 */

public class PushModel {
    private String tin;
    private int product_sub_id;
    private int woreda_id;
    private String title;
    private String quantity;
    private String description;


    public PushModel(String tin, int product_sub_id, int woreda_id, String title, String quantity, String description) {
        this.tin = tin;
        this.product_sub_id = product_sub_id;
        this.woreda_id = woreda_id;
        this.title = title;
        this.quantity = quantity;
        this.description = description;
    }
}
