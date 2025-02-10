package com.crypto.crypt.model.dto;

import com.crypto.crypt.model.Utilisateur;
import org.entityframework.tools.Col;
import org.entityframework.tools.FK;
import org.entityframework.tools.Table;

@Table("v_user_analyse")
public class UserAnalysis {
    @FK(Utilisateur.class)
    @Col("id_utilisateur")
    private Utilisateur utilisateur;
    private double total_achat;
    private double total_vente;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public double getTotal_achat() {
        return total_achat;
    }

    public void setTotal_achat(double total_achat) {
        this.total_achat = total_achat;
    }

    public double getTotal_vente() {
        return total_vente;
    }

    public void setTotal_vente(double total_vente) {
        this.total_vente = total_vente;
    }
}
