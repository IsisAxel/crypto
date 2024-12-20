package com.crypto.crypt.model.tiers;

import com.crypto.crypt.model.Crypto;

public class CryptoValeur {
    private Crypto crypto;
    private double valeur;

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
