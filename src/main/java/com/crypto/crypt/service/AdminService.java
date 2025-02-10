package com.crypto.crypt.service;

import com.crypto.crypt.model.DemandeTransaction;
import com.crypto.crypt.model.Etat;
import com.crypto.crypt.model.Type;
import com.crypto.crypt.model.Utilisateur;

import java.sql.Timestamp;
import java.util.List;

public class AdminService extends Service {
    public AdminService() {
        super();
    }

    public List<DemandeTransaction> getDemandesAttente() throws Exception {
        return getNgContext().findWhereArgs(DemandeTransaction.class, "id_etat = 1 order by date_demande desc");
    }

    public void repondre(int idDemande, boolean estValider) throws Exception {
        Etat e = (estValider) ? getNgContext().findById(2, Etat.class) : getNgContext().findById(3, Etat.class);

        DemandeTransaction dt = getNgContext().findById(idDemande, DemandeTransaction.class);

        //1 = etat attente
        if (dt.getEtat().getId_etat() != 1) {
            throw new Exception("Request already answered or deleted");
        }

        dt.setEtat(e);
        dt.setDate_reponse(new Timestamp(System.currentTimeMillis()));

        getNgContext().update(dt);

        if (!estValider) {
            FirebaseService.updateDemande(dt.getKey(), e.getDesignation());
            FirebaseService.sendNotification(dt, estValider, dt.getUtilisateur().getMonnaie());
            return;
        }

        Type type = dt.getType();

        UserService userService = new UserService(getNgContext());

        if (type.getEtat().equalsIgnoreCase("up")) {
            //depot
            userService.depot(dt.getUtilisateur(), dt.getValeur(), dt.getId_demande());

        } else if (type.getEtat().equalsIgnoreCase("down")) {
            //retrait
            userService.retrait(dt.getUtilisateur(), dt.getValeur(), dt.getId_demande());
        }

        Utilisateur updatedU = getNgContext().findById(dt.getUtilisateur().getId_utilisateur(), Utilisateur.class);

        FirebaseService.updateUserMonnaie(updatedU.getId_utilisateur(), updatedU.getMonnaie());
        FirebaseService.updateDemande(dt.getKey(), e.getDesignation());
        FirebaseService.sendNotification(dt, true, updatedU.getMonnaie());
    }



    public void supprimerDemande(int idDemande) throws Exception {
        DemandeTransaction dt = getNgContext().findById(idDemande, DemandeTransaction.class);

        if (dt.getEtat().getId_etat() != 1) {
            throw new Exception("Request already answered");
        }

        Etat supprimer = getNgContext().findById(4, Etat.class);
        dt.setEtat(supprimer);

        getNgContext().update(dt);
        FirebaseService.updateDemande(dt.getKey(), supprimer.getDesignation());
    }
}
