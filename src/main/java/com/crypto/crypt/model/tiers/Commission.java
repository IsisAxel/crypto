package com.crypto.crypt.model.tiers;

import org.entityframework.tools.Primary;

public class Commission {
    @Primary(auto = true)
    private int id_commission;
    private double commission_achat;
    public double getCommission_achat() {
        return commission_achat;
    }
    public void setCommission_achat(double commission_achat) {
        this.commission_achat = commission_achat;
    }
    private double commission_vente;
    public double getCommission_vente() {
        return commission_vente;
    }
    public void setCommission_vente(double commission_vente) {
        this.commission_vente = commission_vente;
    }
}
