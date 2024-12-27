package com.crypto.crypt.service;

import com.crypto.crypt.model.PortefeuilleUser;
import com.crypto.crypt.model.TransactionCrypto;
import com.crypto.crypt.model.TransactionFond;
import com.crypto.crypt.model.Type;
import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.model.tiers.SessionUser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserService extends Service {
    public UserService() {
        super();
    }

    public void depot(int idUser, double valeur) throws Exception {
        Utilisateur u = getNgContext().findById(idUser, Utilisateur.class);

        Type up = getNgContext().findById(1, Type.class);

        TransactionFond transactionFond = new TransactionFond();
        transactionFond.setIdUtilisateur(u.getId_utilisateur());
        transactionFond.setType(up);
        transactionFond.setValeur(valeur);
        transactionFond.setDate_action(new Timestamp(System.currentTimeMillis()));

        getNgContext().save(transactionFond);
    }

    public int saveUser(Utilisateur u) throws Exception {
        return (int) getNgContext().save(u);
    }

    public void createSession(SessionUser session) throws Exception {
        getNgContext().save(session);
    }

     public List<TransactionCrypto> getAllTransactionCrypto(int id) throws Exception {
        return getNgContext().findWhereArgs(TransactionCrypto.class, "id_utilisateur = ?", id);
    }

    public List<TransactionFond> getAllTransactionFond(int id) throws Exception {
        return getNgContext().findWhereArgs(TransactionFond.class, "id_utilisateur = ?", id);
    }

    public Utilisateur findUtilisateurF(int f_id) throws Exception {
        List<Utilisateur> Utilisateurs = getNgContext().findWhereArgs(Utilisateur.class, "f_id = ?", f_id);

        if (Utilisateurs.isEmpty()) {
            throw new Exception("Utilisateur avec f_id = " + f_id + " introuvable");
        }

        return Utilisateurs.get(0);
    }

    public Utilisateur findUtilisateur(int id) throws Exception {
        List<Utilisateur> Utilisateurs = getNgContext().findWhereArgs(Utilisateur.class, "id_utilisateur = ?", id);

        if (Utilisateurs.isEmpty()) {
            throw new Exception("Utilisateur avec id_utilisateur = " + id + " introuvable");
        }

        return Utilisateurs.get(0);
    }

    public Utilisateur getUserInfo(int id) throws Exception {
        Utilisateur u = findUtilisateur(id);

        u.setTransactionFond(getAllTransactionFond(id));
        u.setTransactionCryptos(getAllTransactionCrypto(id));
       
        try (CryptoService cService = new CryptoService(getNgContext())) {
            PortefeuilleUser pUser = cService.getPortefeuilleUser(id);
            u.setPortefeuille(pUser);
        }

        return u;
    }
}
