package com.crypto.crypt.service;

import com.crypto.crypt.model.*;
import com.crypto.crypt.model.tiers.CryptoValeur;
import com.crypto.crypt.model.tiers.DataChart;
import com.crypto.crypt.model.tiers.Portefeuille;

import java.util.Collections;
import java.util.List;

import org.entityframework.client.GenericEntity;

public class CryptoService extends Service{
    public CryptoService() {
        super();
    }

    public CryptoService(GenericEntity ng) {
        super(ng.getConnection());
    }

    public List<Crypto> getAllCrypto() throws Exception {
        return getAll(Crypto.class);
    }

    public List<TransactionCrypto> getAllTransactionCrypto() throws Exception {
        return getAll(TransactionCrypto.class);
    }

    public PortefeuilleUser getPortefeuilleUser(int idUser) throws Exception {
        List<Portefeuille> portefeuilles = getNgContext().findWhereArgs(Portefeuille.class, "id_utilisateur = ?", idUser);
        PortefeuilleUser portefeuilleUser = new PortefeuilleUser();
        portefeuilleUser.setUtilisateur(getNgContext().findById(idUser, Utilisateur.class));

        List<Cour> cours = dernierCours();
        for (Cour cour : cours) {
            CryptoValeur vl = new CryptoValeur();

            Crypto crypto = cour.getCrypto();
            vl.setCrypto(crypto);
            vl.setValeur(0);
            for (Portefeuille portefeuille : portefeuilles) {
                if (crypto.getId_crypto() == portefeuille.getCrypto().getId_crypto()) {
                    vl.setValeur(portefeuille.getQuantite());
                    break;
                }
            }

            vl.setEstimation(cour.getValeur() * vl.getValeur());
            portefeuilleUser.addCryptoValeur(vl);
        }

        return portefeuilleUser;
    }

    public List<Cour> dernierCours() throws Exception {
        return getNgContext().executeToList(Cour.class, "select * from vue_dernier_cours");
    }

    public void generateCours() throws Exception {
        getNgContext().execute("SELECT generer_cours_crypto()");
    }

    public Object getCryptoData(int idCrypto, int limit) throws Exception {
        List<DataChart> dc = getNgContext().executeToList(DataChart.class, "select valeur, date_changement from cour where id_crypto = ? order by date_changement desc limit ?", idCrypto, limit);
        Collections.reverse(dc);

        return dc;
    }
}
