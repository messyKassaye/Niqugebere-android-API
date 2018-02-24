package com.example.meseret.niqugebere.profile.transporter.transporterModel;

/**
 * Created by Meseret on 2/17/2018.
 */

public class Paths {
    private String id;
    private String start;
    private String end;

    public Paths(String id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
