package com.crypto.crypt.model.dto;

public class TransactionFondDTO {
    private String email;
    private double solde;
    private String key;


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
  
    public double getSolde() {
        return solde;
    }
    public void setSolde(double solde) {
        this.solde = solde;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    } 
}
