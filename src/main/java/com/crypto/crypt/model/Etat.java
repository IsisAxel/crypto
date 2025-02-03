package com.crypto.crypt.model;

import org.entityframework.tools.Primary;

public class Etat {
    @Primary(auto = true)
    private int id_etat;
    private String designation;

    public int getId_etat() {
        return id_etat;
    }

    public void setId_etat(int id_etat) {
        this.id_etat = id_etat;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
