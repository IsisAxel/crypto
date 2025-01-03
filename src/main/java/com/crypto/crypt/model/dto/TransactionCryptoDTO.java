package com.crypto.crypt.model.dto;

public class TransactionCryptoDTO {
    private double quantity;
    private int idCrypto;
    private String key;


    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public int getIdCrypto() {
        return idCrypto;
    }
    public void setIdCrypto(int idCrypto) {
        this.idCrypto = idCrypto;
    }
}
