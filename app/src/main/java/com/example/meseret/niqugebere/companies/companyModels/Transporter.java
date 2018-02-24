package com.example.meseret.niqugebere.companies.companyModels;

/**
 * Created by Meseret on 1/25/2018.
 */

public class Transporter {
    private String id;
    private String driver_name;
    private String type_of_car;
    private String weight;
    private String car_photo;

    public Transporter(String id, String driver_name, String type_of_car, String weight, String car_photo) {
        this.id = id;
        this.driver_name = driver_name;
        this.type_of_car = type_of_car;
        this.weight = weight;
        this.car_photo = car_photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getType_of_car() {
        return type_of_car;
    }

    public void setType_of_car(String type_of_car) {
        this.type_of_car = type_of_car;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCar_photo() {
        return car_photo;
    }

    public void setCar_photo(String car_photo) {
        this.car_photo = car_photo;
    }
}
