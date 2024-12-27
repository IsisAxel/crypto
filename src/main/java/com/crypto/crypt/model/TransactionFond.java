package com.crypto.crypt.model;

import java.sql.Timestamp;

import org.entityframework.tools.Col;
import org.entityframework.tools.Primary;
import org.entityframework.tools.Table;

@Table(name = "transaction_fond")
public class TransactionFond {
    @Primary(auto = true)
    private int id_transaction_fond;
    @Col(name = "id_type", reference = "actif")
    private Type type;
    private int id_utilisateur;
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

    public int getIdUtilisateur() {
        return id_utilisateur;
    }

    public void setIdUtilisateur(int id) {
        this.id_utilisateur = id;
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
