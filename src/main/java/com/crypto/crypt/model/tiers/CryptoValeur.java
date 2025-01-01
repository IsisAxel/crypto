package com.crypto.crypt.model.tiers;

import com.crypto.crypt.model.Crypto;

public class CryptoValeur {
    private Crypto crypto;
    private double valeur;
    private double estimation;

    public double getEstimation() {
        return estimation;
    }

    public void setEstimation(double estimation) {
        this.estimation = estimation;
    }

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
}
