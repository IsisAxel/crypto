package com.crypto.crypt.model;

import org.entityframework.tools.Primary;

public class Utilisateur {
    @Primary(auto = true)
    private int id_utilisateur;
    private int f_id;
    private String nom;
    private double monnaie;

    public Utilisateur() {}

    public Utilisateur(int f_id, String nom) {
        setF_id(f_id);
        setNom(nom);
        setMonnaie(0);
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

    public void setMonnaie(double monnaie) {
        this.monnaie = monnaie;
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }
}
