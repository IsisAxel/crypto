package com.crypto.crypt.model;

public class Utilisateur {
    private int id_utilisateur;
    private String nom;
    private double monnaie;

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

    public void setMonnaie(double monnaie) {
        this.monnaie = monnaie;
    }
}
