package com.crypto.crypt.service;

import com.crypto.crypt.model.PortefeuilleUser;
import com.crypto.crypt.model.TransactionCrypto;
import com.crypto.crypt.model.TransactionFond;
import com.crypto.crypt.model.Type;
import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.model.dto.TransactionFondDTO;
import com.crypto.crypt.model.tiers.SessionUser;
import com.crypto.crypt.model.tiers.ValidationKey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

public class UserService extends Service {
    public UserService() {
        super();
    }

    public void depot(Utilisateur u, double valeur) throws Exception {
        Type up = getNgContext().findById(1, Type.class);

        TransactionFond transactionFond = new TransactionFond();
        transactionFond.setIdUtilisateur(u.getId_utilisateur());
        transactionFond.setType(up);
        transactionFond.setValeur(valeur);
        transactionFond.setDate_action(new Timestamp(System.currentTimeMillis()));

        getNgContext().save(transactionFond);
    }

    public void retrait(Utilisateur u, double valeur) throws Exception {
        Type down = getNgContext().findById(2, Type.class);

        if (u.getMonnaie() < valeur) {
            throw new Exception("Solde Insuffisant");
        }

        TransactionFond transactionFond = new TransactionFond();
        transactionFond.setIdUtilisateur(u.getId_utilisateur());
        transactionFond.setType(down);
        transactionFond.setValeur(valeur);
        transactionFond.setDate_action(new Timestamp(System.currentTimeMillis()));

        getNgContext().save(transactionFond);
    }

    public void transaction(String type, TransactionFondDTO data) throws Exception {
        List<ValidationKey> validationKeys = getNgContext().findWhereArgs(ValidationKey.class, "key = ?", data.getKey());

        if (validationKeys.isEmpty()) {
            throw new Exception("Clé Invalide");
        }

        Utilisateur u = findUtilisateurByEmail(data.getEmail());
        ValidationKey vKey = validationKeys.get(0);

        if (!vKey.getEmail().equals(data.getEmail())) {
            throw new Exception("Clé Invalide");
        }
        
        if (type.equalsIgnoreCase("depot")) {
            depot(u, data.getSolde());

        } else if (type.equalsIgnoreCase("retrait")) {
            retrait(u, data.getSolde());
        }

        //getNgContext().delete(validationKeys.get(0));
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

    public Utilisateur findUtilisateurByEmail(String email) throws Exception {
        List<Utilisateur> Utilisateurs = getNgContext().findWhereArgs(Utilisateur.class, "email = ?", email);

        if (Utilisateurs.isEmpty()) {
            throw new Exception("Utilisateur avec email = " + email + " introuvable");
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

    public void requestValidation(int id) throws Exception {
        Utilisateur u = findUtilisateur(id);

        ValidationKey vKey = new ValidationKey();
        vKey.setEmail(u.getEmail());
        vKey.setKey(generateHash());

        getNgContext().save(vKey);
    }

    public static String generateHash() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        Random random = new Random();
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 10; i++) { 
            input.append((char)(random.nextInt(26) + 'a')); 
        }
        byte[] hashBytes = digest.digest(input.toString().getBytes());

        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            hash.append(String.format("%02x", b));
        }

        return hash.toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 30);         
    }
}
