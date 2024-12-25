package com.crypto.crypt.model;

import org.entityframework.tools.Primary;

public class Crypto {
    @Primary
    private int id_crypto;
    private String nom;

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
