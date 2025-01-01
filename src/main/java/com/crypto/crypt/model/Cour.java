package com.crypto.crypt.model;

import org.entityframework.tools.Col;

//import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Cour {
    @Col(name = "id_crypto", reference = "active")
    private Crypto crypto;
    private double valeur;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Indian/Antananarivo")
    private Timestamp date_changement;

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public Timestamp getDate_changement() {
        return date_changement;
    }

    public void setDate_changement(Timestamp date_changement) {
        this.date_changement = date_changement;
    }
}
