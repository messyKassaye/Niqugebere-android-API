package com.example.meseret.niqugebere.profile.transporter.transporterModel;

/**
 * Created by Meseret on 2/18/2018.
 */

public class Vehicles {
    private String plate_no;
    private String capacity;

    public Vehicles(String plate_no, String capacity) {
        this.plate_no = plate_no;
        this.capacity = capacity;
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
