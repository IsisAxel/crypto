package com.crypto.crypt.dao;

import org.entityframework.dev.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DataSource {
    // public static Connection getConnection() throws SQLException, ClassNotFoundException {
    //     return Driver.getPGConnection("postgres", "postgres", "crypto_data");
    // }

    public static Connection getConnection() throws SQLException {
        // L'URL utilise le nom du service Docker 'postgres' pour se connecter
        String url = "jdbc:postgresql://postgres:5432/crypto_data"; // Nom du service Docker comme hôte
        String username = "postgres";
        String password = "postgres";

        // Tentative de connexion à la base de données PostgreSQL
        return DriverManager.getConnection(url, username, password);
    }
}
