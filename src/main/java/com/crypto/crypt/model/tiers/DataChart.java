package com.crypto.crypt.model.tiers;

import java.sql.Timestamp;

//import com.fasterxml.jackson.annotation.JsonFormat;

public class DataChart {
    private double valeur;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Indian/Antananarivo")
    private Timestamp date_changement;

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
