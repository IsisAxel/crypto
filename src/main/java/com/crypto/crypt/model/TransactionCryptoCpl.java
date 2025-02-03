package com.crypto.crypt.model;

import org.entityframework.tools.Col;
import org.entityframework.tools.FK;
import org.entityframework.tools.Table;

@Table("transaction_crypto")
public class TransactionCryptoCpl extends TransactionCrypto {
    @FK(Utilisateur.class)
    @Col("id_utilisateur")
    private Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
