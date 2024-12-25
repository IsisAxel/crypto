package com.crypto.crypt.model;

import com.crypto.crypt.model.tiers.CryptoValeur;

import java.util.ArrayList;
import java.util.List;

public class PortefeuilleUser {
    private Utilisateur utilisateur;
    private List<CryptoValeur> cryptoValeurs;

    public PortefeuilleUser() {
        cryptoValeurs = new ArrayList<>();
    }

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

    public void addCryptoValeur(CryptoValeur cryptoValeur) {
        cryptoValeurs.add(cryptoValeur);
    }
}
