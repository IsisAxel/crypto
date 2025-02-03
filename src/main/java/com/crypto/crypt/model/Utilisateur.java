package com.crypto.crypt.model;

import java.util.List;
import java.util.Map;

import org.entityframework.tools.Col;
import org.entityframework.tools.Primary;
import org.entityframework.tools.Transient;

public class Utilisateur {
    @Primary(auto = true)
    private int id_utilisateur;
    private int f_id;
    private String nom;
    private String email;
    private double monnaie;
    private String imageUrl;

    public Map<String, Object> toFirebaseMap() {
        return Map.of(
                "id_utilisateur", getId_utilisateur(),
                "f_id", getF_id(),
                "nom", getNom(),
                "email", getEmail(),
                "monnaie", getMonnaie(),
                "imageUrl", "0"
        );
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Transient
    private PortefeuilleUser portefeuille;

    @Transient
    private List<TransactionFond> transactionFond;

    @Transient
    private List<TransactionCrypto> transactionCryptos;

    public List<TransactionCrypto> getTransactionCryptos() {
        return transactionCryptos;
    }

    public void setTransactionCryptos(List<TransactionCrypto> transactionCryptos) {
        this.transactionCryptos = transactionCryptos;
    }

    public List<TransactionFond> getTransactionFond() {
        return transactionFond;
    }

    public void setTransactionFond(List<TransactionFond> transactionFond) {
        this.transactionFond = transactionFond;
    }

    public PortefeuilleUser getPortefeuille() {
        return portefeuille;
    }

    public void setPortefeuille(PortefeuilleUser portefeuille) {
        this.portefeuille = portefeuille;
    }

    public Utilisateur() {}

    public Utilisateur(int f_id, String nom, String email) throws Exception {
        setF_id(f_id);
        setNom(nom);
        setMonnaie(0);
        setEmail(email);
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(double monnaie) throws Exception {
        if (monnaie < 0) {
            throw new Exception("System error, please send feedback");
        }
        this.monnaie = monnaie;
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
