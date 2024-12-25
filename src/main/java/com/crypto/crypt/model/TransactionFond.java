package com.crypto.crypt.model;

import java.sql.Timestamp;

public class TransactionFond {
    private int id_transaction_fond;
    private Type type;
    private Utilisateur utilisateur;
    private double valeur;
    private Timestamp date_action;

    public int getId_transaction_fond() {
        return id_transaction_fond;
    }

    public void setId_transaction_fond(int id_transaction_fond) {
        this.id_transaction_fond = id_transaction_fond;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public Timestamp getDate_action() {
        return date_action;
    }

    public void setDate_action(Timestamp date_action) {
        this.date_action = date_action;
    }
}
