package com.crypto.crypt.model;

import org.entityframework.tools.Col;
import org.entityframework.tools.FK;
import org.entityframework.tools.Primary;
import org.entityframework.tools.Table;

import java.sql.Timestamp;
import java.util.Map;

@Table("demande_transaction_fond")
public class DemandeTransaction {
    @Primary(auto = true)
    @Col("id_demande_transaction_fond")
    private int id_demande;
    @FK(Utilisateur.class)
    @Col("id_utilisateur")
    private Utilisateur utilisateur;
    private double valeur;
    private Timestamp date_demande;
    private Timestamp date_reponse;
    @FK(Etat.class)
    @Col("id_etat")
    private Etat etat;
    @FK(Type.class)
    @Col("id_type")
    private Type type;

    public Map<String, Object> toFirebaseMap() {
        String type = (getType().getEtat().equalsIgnoreCase("up")) ? "deposit" : "withdraw";
        return Map.of(
            "id_demande", getId_demande(),
            "email", getUtilisateur().getEmail(),
            "valeur", getValeur(),
            "date_demande", new Timestamp(getDate_demande().getTime()),
            "type", type,
            "etat", getEtat().getDesignation()
        );
    }

    public int getId_demande() {
        return id_demande;
    }

    public void setId_demande(int id_demande) {
        this.id_demande = id_demande;
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

    public Timestamp getDate_demande() {
        return date_demande;
    }

    public void setDate_demande(Timestamp date_demande) {
        this.date_demande = date_demande;
    }

    public Timestamp getDate_reponse() {
        return date_reponse;
    }

    public void setDate_reponse(Timestamp date_reponse) {
        this.date_reponse = date_reponse;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
