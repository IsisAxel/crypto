package com.crypto.crypt.model;

import java.sql.Timestamp;
import java.util.Map;

import org.entityframework.tools.Col;
import org.entityframework.tools.FK;
import org.entityframework.tools.Primary;
import org.entityframework.tools.Table;

@Table("transaction_crypto")
public class TransactionCrypto {
    @Primary(auto = true)
    private int id_transaction_crypto;
    @Col("id_crypto")
    @FK(Crypto.class)
    private Crypto crypto;
    private int id_utilisateur;
    private double cour;
    private Timestamp date_action;
    private double commission;
    private double total;
    private double total_with_commission;
    @Col("id_type")
    @FK(Type.class)
    private Type type;
    private double qtty;

    public Map<String, Object> toFirebaseMap(String email) {
        String type = (getType().getEtat().equalsIgnoreCase("up")) ? "buy" : "sell";
        return Map.of(
                "id_transaction_crypto", getId_transaction_crypto(),
                "crypto", getCrypto(),
                "cour", getCour(),
                "date_action", new Timestamp(getDate_action().getTime()),
                "total_with_commission", getTotal_with_commission(),
                "type", type,
                "qtty", qtty,
                "email", email
        );
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal_with_commission() {
        return total_with_commission;
    }

    public void setTotal_with_commission(double total_with_commission) {
        this.total_with_commission = total_with_commission;
    }

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

    public void setQtty(double qtty) throws Exception {
        if (qtty <= 0) {
            throw new Exception("Invalid quantity");
        }
        this.qtty = qtty;
    }
}
