package com.crypto.crypt.model;


import org.entityframework.tools.FK;
import org.entityframework.tools.Primary;
import org.entityframework.tools.Table;

import java.util.Map;

@Table("crypto_favori")
public class CryptoFavori {
    @Primary(auto = true)
    private int id_crypto_favori;
    @FK(Crypto.class)
    private int id_crypto;
    @FK(Utilisateur.class)
    private int id_utilisateur;
    private String key;

    public Map<String, Object> toFirebaseMap(String email) {
        return Map.of(
            "id_crypto", getId_crypto(),
            "email", email
        );
    }

    public int getId_crypto() {
        return id_crypto;
    }

    public void setId_crypto(int id_crypto) {
        this.id_crypto = id_crypto;
    }

    public int getId_crypto_favori() {
        return id_crypto_favori;
    }

    public void setId_crypto_favori(int id_crypto_favori) {
        this.id_crypto_favori = id_crypto_favori;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
