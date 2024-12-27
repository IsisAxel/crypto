package com.crypto.crypt.model.tiers;

import org.entityframework.tools.Primary;

import com.crypto.crypt.model.Crypto;

public class Portefeuille {
    @Primary(auto = true)
    private int id_portefeuille;
    private Crypto crypto;
    private int id_utilisateur;
    private double quantite;

    public int getId_portefeuille() {
        return id_portefeuille;
    }

    public void setId_portefeuille(int id_portefeuille) {
        this.id_portefeuille = id_portefeuille;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }
}
