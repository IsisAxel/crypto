package com.crypto.crypt.model;

import java.sql.Timestamp;

import org.entityframework.tools.Col;
import org.entityframework.tools.Primary;
import org.entityframework.tools.Table;

@Table(name = "transaction_crypto")
public class TransactionCrypto {
    @Primary(auto = true)
    private int id_transaction_crypto;
    @Col(name = "id_crypto" , reference = "id_crypto")
    private Crypto crypto;
    private int id_utilisateur;
    private double cour;
    private Timestamp date_action;

    @Col(name = "id_type" , reference = "id_type")
    private Type type;
    private double qtty;


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

    public int getIdUtilisateur() {
        return id_utilisateur;
    }

    public void setIdUtilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
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

    public double getQtty() {
        return qtty;
    }

    public void setQtty(double qtty) {
        this.qtty = qtty;
    }
}
