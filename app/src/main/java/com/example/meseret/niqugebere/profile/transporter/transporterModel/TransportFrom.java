package com.example.meseret.niqugebere.profile.transporter.transporterModel;

/**
 * Created by Meseret on 2/18/2018.
 */

public class TransportFrom {
    private String from_company;
    private String from_woreda;

    public TransportFrom(String from_company, String from_woreda) {
        this.from_company = from_company;
        this.from_woreda = from_woreda;
    }

    public String getFrom_company() {
        return from_company;
    }

    public void setFrom_company(String from_company) {
        this.from_company = from_company;
    }

    public String getFrom_woreda() {
        return from_woreda;
    }

    public void setFrom_woreda(String from_woreda) {
        this.from_woreda = from_woreda;
    }
}
