package com.crypto.crypt.model.tiers;

import java.sql.Timestamp;

public class DataChart {
    private double valeur;
    public double getValeur() {
        return valeur;
    }
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    private Timestamp date_changement;
    public Timestamp getDate_changement() {
        return date_changement;
    }
    public void setDate_changement(Timestamp date_changement) {
        this.date_changement = date_changement;
    }
    
}
