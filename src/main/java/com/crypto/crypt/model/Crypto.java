package com.crypto.crypt.model;

import org.entityframework.tools.Primary;

public class Crypto {
    @Primary
    private int id_crypto;
    private String nom;
    private String unit_nom;
    public String getUnit_nom() {
        return unit_nom;
    }

    public void setUnit_nom(String unit_nom) {
        this.unit_nom = unit_nom;
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId_crypto() {
        return id_crypto;
    }

    public void setId_crypto(int id_crypto) {
        this.id_crypto = id_crypto;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
