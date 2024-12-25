package com.crypto.crypt.model;

import java.sql.Timestamp;

public class TransactionCrypto {
    private int id_transaction_crypto;
    private Crypto crypto;
    private Utilisateur utilisateur;
    private double cour;
    private Timestamp date_action;
    private Type type;

    public int getId_transaction_crypto() {
        return id_transaction_crypto;
    }

    public void setId_transaction_crypto(int id_transaction_crypto) {
        this.id_transaction_crypto = id_transaction_crypto;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public double getCour() {
        return cour;
    }

    public void setCour(double cour) {
        this.cour = cour;
    }

    public Timestamp getDate_action() {
        return date_action;
    }

    public void setDate_action(Timestamp date_action) {
        this.date_action = date_action;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
