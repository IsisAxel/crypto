package com.crypto.crypt.model.tiers;

import java.sql.Timestamp;

import org.entityframework.tools.Primary;

public class SessionUser {
    @Primary(auto = true)
    private int id_session;
    private String token;
    private String f_token;
    private Timestamp expiration;
    private int id_utilisateur;

    public SessionUser(String token, String f_token, Timestamp expiration, int id_utilisateur) {
        this.token = token;
        this.f_token = f_token;
        this.expiration = expiration;
        this.id_utilisateur = id_utilisateur;
    }

    public SessionUser() {}

    
    public Timestamp getExpiration() {
        return expiration;
    }
    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }
    public String getF_token() {
        return f_token;
    }
    public void setF_token(String f_token) {
        this.f_token = f_token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getId_session() {
        return id_session;
    }
    public void setId_session(int id_session) {
        this.id_session = id_session;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
}
