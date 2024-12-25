package com.crypto.crypt.service;

import com.crypto.crypt.model.*;
import com.crypto.crypt.model.tiers.CryptoValeur;
import com.crypto.crypt.model.tiers.Portefeuille;

import java.util.List;

public class CryptoService extends Service{
    public CryptoService() {
        super();
    }

    public List<Crypto> getAllCrypto() throws Exception {
        return getAll(Crypto.class);
    }

    public List<TransactionCrypto> getAllTransactionCrypto() throws Exception {
        return getAll(TransactionCrypto.class);
    }

    public PortefeuilleUser getPortefeuilleUser(int idUser) throws Exception {
        List<Crypto> cryptoList = getAllCrypto();

        List<Portefeuille> portefeuilles = getNgContext().findWhereArgs(Portefeuille.class, "id_utilisateur = ?", idUser);
        PortefeuilleUser portefeuilleUser = new PortefeuilleUser();
        portefeuilleUser.setUtilisateur(getNgContext().findById(idUser, Utilisateur.class));

        for (Crypto crypto : cryptoList) {
            CryptoValeur vl = new CryptoValeur();
            vl.setCrypto(crypto);
            vl.setValeur(0);
            for (Portefeuille portefeuille : portefeuilles) {
                if (crypto.getId_crypto() == portefeuille.getCrypto().getId_crypto()) {
                    vl.setValeur(portefeuille.getQuantite());
                    break;
                }
            }
            portefeuilleUser.addCryptoValeur(vl);
        }

        return portefeuilleUser;
    }

    public List<Cour> dernierCours() throws Exception {
        return getNgContext().executeToList(Cour.class, "select * from vue_dernier_cours");
    }

    public void generateCours(double min, double max) throws Exception {
        getNgContext().execute("SELECT generer_cours_crypto(?, ?)", min, max);
    }
}
