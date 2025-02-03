package com.crypto.crypt.model;

import org.entityframework.tools.Col;
import org.entityframework.tools.FK;
import org.entityframework.tools.Table;

import java.sql.Timestamp;

@Table("operations_utilisateurs")
public class Operation {
    @FK(Utilisateur.class)
    @Col("id_utilisateur")
    private Utilisateur utilisateur;

    private String type_transaction_str;
    @FK(Type.class)
    @Col("id_type_transaction")
    private Type type;

    private double Cour;
    @FK(Crypto.class)
    @Col("id_crypto")
    private Crypto crypto;

    private double valeur;
    private Timestamp date_action;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getType_transaction_str() {
        return type_transaction_str;
    }

    public void setType_transaction_str(String type_transaction_str) {
        this.type_transaction_str = type_transaction_str;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getCour() {
        return Cour;
    }

    public void setCour(double cour) {
        Cour = cour;
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

    public Timestamp getDate_action() {
        return date_action;
    }

    public void setDate_action(Timestamp date_action) {
        this.date_action = date_action;
    }
}
