package com.crypto.crypt.service;

import com.crypto.crypt.model.Cour;
import com.crypto.crypt.model.Crypto;
import com.crypto.crypt.model.PortefeuilleUser;
import com.crypto.crypt.model.TransactionCrypto;
import com.crypto.crypt.model.TransactionFond;
import com.crypto.crypt.model.Type;
import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.model.dto.FeedbackDTO;
import com.crypto.crypt.model.dto.TransactionCryptoDTO;
import com.crypto.crypt.model.dto.TransactionFondDTO;
import com.crypto.crypt.model.tiers.Commission;
import com.crypto.crypt.model.tiers.Feedback;
import com.crypto.crypt.model.tiers.Portefeuille;
import com.crypto.crypt.model.tiers.SessionUser;
import com.crypto.crypt.model.tiers.ValidationKey;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.entityframework.client.GenericEntity;
import org.entityframework.error.EntityNotFoundException;

public class UserService extends Service {
    public UserService() {
        super();
    }

    public void logout(String token, int id) throws Exception {
        List<SessionUser> s = getNgContext().findWhereArgs(SessionUser.class, "token = ?", token);
        if (!s.isEmpty()) {
            getNgContext().delete(s.get(0));
        }
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
            throw new Exception("Not enough money");
        }

        TransactionFond transactionFond = new TransactionFond();
        transactionFond.setIdUtilisateur(u.getId_utilisateur());
        transactionFond.setType(down);
        transactionFond.setValeur(valeur);
        transactionFond.setDate_action(new Timestamp(System.currentTimeMillis()));

        getNgContext().save(transactionFond);
    }

    private void verifiateKey(String key, String email) throws Exception {
        // List<ValidationKey> validationKeys = getNgContext().findWhereArgs(ValidationKey.class, "key = ?", key);

        // if (validationKeys.isEmpty()) {
        //     throw new Exception("Invalid key");
        // }
        
        // ValidationKey vKey = validationKeys.get(0);

        // if (!vKey.getEmail().equals(email)) {
        //     throw new Exception("Invalid key");
        // }
    }

    public void transaction(int idUser, String type, TransactionFondDTO data) throws Exception {
        verifiateKey(data.getKey(), data.getEmail());
        Utilisateur u = findUtilisateurByEmail(data.getEmail());

        if (u.getId_utilisateur() != idUser) {
            throw new Exception("Unauthorized transaction");
        }
        
        if (type.equalsIgnoreCase("depot")) {
            depot(u, data.getSolde());

        } else if (type.equalsIgnoreCase("retrait")) {
            retrait(u, data.getSolde());
        }
    }

    public int saveUser(Utilisateur u) throws Exception {
        return (int) getNgContext().save(u);
    }

    public void createSession(SessionUser session) throws Exception {
        getNgContext().save(session);
    }

     public List<TransactionCrypto> getAllTransactionCrypto(int id) throws Exception {
        return getNgContext().findWhereArgs(TransactionCrypto.class, "id_utilisateur = ? order by date_action desc", id);
    }

    public List<TransactionFond> getAllTransactionFond(int id) throws Exception {
        return getNgContext().findWhereArgs(TransactionFond.class, "id_utilisateur = ? order by date_action desc", id);
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

    public Utilisateur getUser(int id) throws Exception {
        return findUtilisateur(id);
    }


    public Portefeuille getUserWallet(int idUser , int idCrypto) throws Exception {
        Crypto crypto = null;
        try {
            crypto = getNgContext().findById(idCrypto, Crypto.class);
        } catch (Exception e) {
            if (e instanceof EntityNotFoundException) {
                throw new Exception("Crypto id invalid");
            }
            throw e;
        }
       
        List<Portefeuille> portefeuilles = getNgContext().findWhereArgs(Portefeuille.class, "id_utilisateur = ? and id_crypto = ?", idUser, idCrypto);

       if (crypto == null) {
            throw new Exception("Crypto id invalid");
       }
            
        if (portefeuilles.isEmpty()) {
            Portefeuille p = new Portefeuille();
            p.setId_utilisateur(idUser);
            p.setCrypto(crypto);
            p.setQuantite(0.0);
            return p;
        }

        return portefeuilles.get(0);
    }

    public void requestValidation(int id) throws Exception {
        Utilisateur u = findUtilisateur(id);

        ValidationKey vKey = new ValidationKey();
        vKey.setEmail(u.getEmail());
        vKey.setKey(generateHash());

        String validationHtml = MailService.generateEmailHtml(vKey.getKey());
        try {
            //MailService.sendEmail(u.getEmail(), validationHtml);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Email not sent");
        }
        
        getNgContext().executeUpdate("delete from key_validation_email where email = ?", u.getEmail());
        getNgContext().save(vKey);
    }

    private static String generateHash() throws Exception {
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

    public void buyCrypto(TransactionCryptoDTO data, int idUser) throws Exception {
        Utilisateur u = findUtilisateur(idUser);
        verifiateKey(data.getKey(), u.getEmail());
        
        if (u.getId_utilisateur() != idUser) {
            throw new Exception("Unauthorized transaction");
        }

        TransactionCrypto trans = new TransactionCrypto();
        Cour c = GenericEntity.first(getNgContext().executeToList(Cour.class, "select * from vue_dernier_cours where id_crypto = ?", data.getIdCrypto()));

        if (c == null) {
            throw new Exception("data invalid");
        }

        Crypto crypto = c.getCrypto();
        Type up = getNgContext().findById(1, Type.class);
        
        double amountToPay = c.getValeur() * data.getQuantity();
        // Commission commission = new CryptoService(getNgContext()).getCommission();
        // amountToPay += (commission.getCommission_achat() * amountToPay) / 100;

        if (u.getMonnaie() < amountToPay) {
            throw new Exception("Not enough money");
        }

        trans.setCour(c.getValeur());
        trans.setCrypto(crypto);
        trans.setIdUtilisateur(idUser);
        trans.setDate_action(new Timestamp(System.currentTimeMillis()));
        trans.setQtty(data.getQuantity());
        trans.setType(up);
        
        getNgContext().save(trans);

        u.setMonnaie(u.getMonnaie() - amountToPay);
        getNgContext().update(u);
    }

    public void sellCrypto(TransactionCryptoDTO data, int idUser) throws Exception {
        Utilisateur u = findUtilisateur(idUser);
        verifiateKey(data.getKey(), u.getEmail());
        
        if (u.getId_utilisateur() != idUser) {
            throw new Exception("Unauthorized transaction");
        }

        TransactionCrypto trans = new TransactionCrypto();
        Cour c = GenericEntity.first(getNgContext().executeToList(Cour.class, "select * from vue_dernier_cours where id_crypto = ?", data.getIdCrypto()));

        if (c == null) {
            throw new Exception("data invalid");
        }

        Crypto crypto = c.getCrypto();
        Type down = getNgContext().findById(2, Type.class);
        
        double amountToEarn = c.getValeur() * data.getQuantity();
        // Commission commission = new CryptoService(getNgContext()).getCommission();
        // amountToEarn -= (commission.getCommission_vente() * amountToEarn) / 100;

        Portefeuille p = getUserWallet(u.getId_utilisateur(), crypto.getId_crypto());
        if (p.getQuantite() < data.getQuantity()) {
            throw new Exception("Not enough Crypto coin");
        }

        trans.setCour(c.getValeur());
        trans.setCrypto(crypto);
        trans.setIdUtilisateur(idUser);
        trans.setDate_action(new Timestamp(System.currentTimeMillis()));
        trans.setQtty(data.getQuantity());
        trans.setType(down);
        
        getNgContext().save(trans);

        u.setMonnaie(u.getMonnaie() + amountToEarn);
        getNgContext().update(u);
    }

    public void feedback(FeedbackDTO feed, int idUser) throws Exception {
        Utilisateur u = findUtilisateur(idUser);

        Feedback feedback = new Feedback();
        feedback.setContent(feed.getContent());
        feedback.setSubject(feed.getSubject());
        feedback.setId_sender(u.getId_utilisateur());
        getNgContext().save(feedback);

        MailService.sendEmail(feedback, u);
    }

    public Commission getCommission() throws Exception {
        List<Commission> coms = getNgContext().findWhen(Commission.class, " order by id_commission desc");

        if (coms.isEmpty()) {
            Commission c = new Commission();
            c.setCommission_achat(0.0);
            c.setCommission_vente(0.0);
            return c;
        }

        return coms.get(0);
    }
}
