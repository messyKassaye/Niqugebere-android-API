package com.example.meseret.niqugebere.model;

/**
 * Created by meseret on 3/2/18.
 */

public class Version {
    private String version_no;
    private String version_name;

    public Version() {
    }

    public Version(String version_no, String version_name) {
        this.version_no = version_no;
        this.version_name = version_name;
    }

    public String getVersion_no() {
        return version_no;
    }

    public void setVersion_no(String version_no) {
        this.version_no = version_no;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }
}
