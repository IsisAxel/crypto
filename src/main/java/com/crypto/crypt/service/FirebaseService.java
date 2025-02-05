package com.crypto.crypt.service;

import com.crypto.crypt.err.NoInternetConnectionException;
import com.crypto.crypt.model.DemandeTransaction;
import com.crypto.crypt.model.TransactionCrypto;
import com.crypto.crypt.model.Utilisateur;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import java.util.Map;

public class FirebaseService extends Service {
    public FirebaseService() {
        super(true);
    }

    public static void saveData(String collectionName, int documentId, Map<String, Object> data) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(collectionName).document(String.valueOf(documentId)).set(data).get();
    }

    public void saveUser(Utilisateur u) throws Exception {
        Map<String, Object> data = u.toFirebaseMap();
        saveData("Utilisateurs", u.getId_utilisateur(), data);
    }

    public static void updateUserMonnaie(int userId, double newMonnaie) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("Utilisateurs")
                .document(String.valueOf(userId))
                .update("monnaie", newMonnaie)
                .get();
    }

    public static void updateDemande(int idDemande, String etat) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("Demandes")
                .document(String.valueOf(idDemande))
                .update("etat", etat)
                .get();
    }

    public static void sendNotification(DemandeTransaction demande, boolean estValider, double newBalance) {
        if (!Service.isOnlineMode()) {
            System.out.println("No Internet Connection : Unable to send notification");
            return;
        }

        String title = estValider ? "Congrats !" : "Sorry !";
        String type = demande.getType().getEtat().equalsIgnoreCase("up") ? "deposit" : "withdraw";
        String body = estValider
                ? "The administrator has accepted your " + type + " request of " + demande.getValeur()
                : "The administrator refused your " + type + " request of " + demande.getValeur();

        String email = demande.getUtilisateur().getEmail();
        Firestore db = FirestoreClient.getFirestore();

        try {
            ApiFuture<QuerySnapshot> future = db.collection("userToken")
                    .whereEqualTo("email", email)
                    .get();

            QuerySnapshot querySnapshot = future.get();

            if (querySnapshot.isEmpty()) {
                System.out.println("No FCM Token for user : " + email);
                return;
            }

            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            String token = document.getString("fcmToken");

            if (token == null || token.isEmpty()) {
                System.out.println("FCM Token is empty for user : " + email);
                return;
            }

            // Ajout du nouveau solde dans les données envoyées
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("type", type)  // Type de transaction (deposit/withdrawal)
                    .putData("amount", String.valueOf(demande.getValeur())) // Montant de la transaction
                    .putData("newBalance", String.valueOf(newBalance)) // Nouveau solde après transaction
                    .putData("estValider", String.valueOf(estValider))
                    .setToken(token)
                    .build();

            FirebaseMessaging.getInstance().send(message);
            System.out.println("Notification sent successfully to user: " + email);

        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }
    }

    public static void saveTransaction(TransactionCrypto transaction, String email) throws Exception {
        if (!Service.isOnlineMode())
            throw new NoInternetConnectionException();

        Map<String, Object> data = transaction.toFirebaseMap(email);
        saveData("Transactions", transaction.getId_transaction_crypto(), data);
    }
}
