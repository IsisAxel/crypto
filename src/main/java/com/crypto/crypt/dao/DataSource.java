package com.crypto.crypt.dao;

import org.entityframework.dev.Driver;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DataSource {
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        return Driver.getPGConnection("postgres", "postgres", "crypto_data");
    }
}
