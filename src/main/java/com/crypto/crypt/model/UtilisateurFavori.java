package com.crypto.crypt.model;

import java.util.List;

public class UtilisateurFavori {
    private int id_utilisateur;
    private List<Crypto> favoris;

    public UtilisateurFavori(int id_utilisateur, List<Crypto> favoris) {
        setFavoris(favoris);
        setId_utilisateur(id_utilisateur);
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public List<Crypto> getFavoris() {
        return favoris;
    }

    public void setFavoris(List<Crypto> favoris) {
        this.favoris = favoris;
    }
}
