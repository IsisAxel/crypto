package com.crypto.crypt.model;

import com.crypto.crypt.model.tiers.CryptoValeur;

import java.util.List;

public class Portefeuille {
    private Utilisateur utilisateur;
    private List<CryptoValeur> cryptoValeurs;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<CryptoValeur> getCryptoValeurs() {
        return cryptoValeurs;
    }

    public void setCryptoValeurs(List<CryptoValeur> cryptoValeurs) {
        this.cryptoValeurs = cryptoValeurs;
    }
}
