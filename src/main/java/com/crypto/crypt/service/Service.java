package com.crypto.crypt.service;

import com.crypto.crypt.dao.DataSource;
import jakarta.annotation.PreDestroy;
import org.entityframework.client.GenericEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Service implements AutoCloseable {
    private final GenericEntity ngContext;

    public Service() {
        try {
            this.ngContext = new GenericEntity(DataSource.getConnection());
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de l'initialisation du service", e);
        }
    }

    public Service(Connection connection) {
        try {
            this.ngContext = new GenericEntity(connection);
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de l'initialisation du service", e);
        }
    }

    public GenericEntity getNgContext() {
        return ngContext;
    }

    public void beginTransaction() throws Exception {
        getNgContext().setAutoCommit(false);
        getNgContext().setLogged(true);
    }

    public void endTransaction() throws Exception {
        getNgContext().commit();
    }

    public Connection getConnection() {
        return getNgContext().getConnection();
    }

    @Override
    @PreDestroy
    public void close() throws SQLException {
        getNgContext().closeConnection();
    }

    public void commit() throws SQLException {
        getNgContext().commit();
    }

    public void rollBack() throws SQLException {
        getNgContext().rollBack();
    }

    public <T> List<T> getAll(Class<T> tClass) throws Exception {
        return getNgContext().findAll(tClass);
    }
}
