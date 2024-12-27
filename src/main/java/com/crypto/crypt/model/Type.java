package com.crypto.crypt.model;

import org.entityframework.tools.Primary;

public class Type {
    @Primary
    private int id_type;
    private String etat;

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
