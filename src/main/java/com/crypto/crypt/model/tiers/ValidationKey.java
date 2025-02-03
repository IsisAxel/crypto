package com.crypto.crypt.model.tiers;

import org.entityframework.tools.Primary;
import org.entityframework.tools.Table;

@Table("key_validation_email")
public class ValidationKey {
    @Primary(auto = true)
    private int id_validation;
    private String email;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId_validation() {
        return id_validation;
    }

    public void setId_validation(int id_validation) {
        this.id_validation = id_validation;
    }
}
