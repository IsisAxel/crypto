package com.crypto.crypt.service;

import com.crypto.crypt.err.NoInternetConnectionException;
import com.crypto.crypt.model.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.*;
import org.entityframework.tools.RowResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseService extends Service {
    public FirebaseService() {
        super(true);
    }

    public FirebaseService(boolean connection) {
        super();
    }

    public static void saveData(String collectionName, int documentId, Map<String, Object> data) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(collectionName).document(String.valueOf(documentId)).set(data).get();
    }

    public static String saveDataG(String collectionName, Map<String, Object> data) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(collectionName).add(data).get();
        return docRef.getId();
    }

    public static void deleteDoc(String collectionName, String documentId) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(collectionName).document(documentId).delete().get();
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

    public static void updateDemande(String key, String etat) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("Demandes")
                .document(key)
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
                ? "The administrator has accepted your " + type + " request of $ " + Service.formatDouble(demande.getValeur())
                : "The administrator refused your " + type + " request of $ " + Service.formatDouble(demande.getValeur());

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

            List<String> tokens = new ArrayList<>();

            // Récupérer tous les tokens associés à l'email
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                String token = document.getString("fcmToken");
                if (token != null && !token.isEmpty()) {
                    tokens.add(token);
                }
            }

            if (tokens.isEmpty()) {
                System.out.println("No valid FCM Tokens for user: " + email);
                return;
            }

            // Envoyer une notification par token
            for (String token : tokens) {
                try {
                    Message message = Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .build())
                            .putData("category", "fond")
                            .putData("type", type)
                            .putData("amount", Service.formatDouble(demande.getValeur()))
                            .putData("newBalance", Service.formatDouble(newBalance))
                            .putData("estValider", String.valueOf(estValider))
                            .setToken(token) // Envoi à UN SEUL token à la fois
                            .build();

                    FirebaseMessaging.getInstance().send(message);
                    System.out.println("Notification envoyée avec succès au token : " + token);
                } catch (FirebaseMessagingException e) {
                    System.out.println("Erreur lors de l'envoi au token " + token + " : " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi de la notification : " + e.getMessage());
        }
    }



    public static void saveTransaction(TransactionCrypto transaction, String email) throws Exception {
        if (!Service.isOnlineMode())
            throw new NoInternetConnectionException();

        Map<String, Object> data = transaction.toFirebaseMap(email);
        saveData("Transactions", transaction.getId_transaction_crypto(), data);
    }

    private static DocumentSnapshot getDocument(String idDocument, String collection, Firestore db) throws Exception {
        DocumentReference docRef = db.collection(collection).document(idDocument);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        DocumentSnapshot document = future.get();

        if (!document.exists()) {
            throw new Exception("Document not found (" + idDocument + ") : " + collection);
        }

        return document;
    }

    public void synchroniser() {
        if (!Service.isOnlineMode()) {
            System.out.println("No internet Connection, Cannot synchronize database");
            return;
        }

        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("Synchro")
                    .orderBy("time", Query.Direction.ASCENDING)
                    .get();
            QuerySnapshot querySnapshot = future.get();

            if (querySnapshot.isEmpty()) {
                System.out.println("No data to sync");
                return;
            }

            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                String entite = doc.getString("entite");
                String id = doc.getString("id");
                String type = doc.getString("type");

                if (entite == null || id == null || type == null) {
                    System.err.println("Mal formed data in Synchro");
                    continue;
                }

                if (type.equalsIgnoreCase("insert")) {
                    if (entite.equalsIgnoreCase("Demande")) {
                        DocumentSnapshot ds = getDocument(id, "Demandes", db);

                        Timestamp date_demande = ds.getTimestamp("date_demande");

                        String etat = ds.getString("etat");
                        Etat e = getNgContext().firstOrNull(Etat.class, "designation = ?", etat);

                        String email = ds.getString("email");
                        Utilisateur u = getNgContext().firstOrNull(Utilisateur.class, "email = ?", email);

                        String typeDs = ds.getString("type");

                        String valeur = ds.getString("valeur");

                        DemandeTransaction dt = new DemandeTransaction();
                        dt.setDate_demande(new java.sql.Timestamp(date_demande.toDate().getTime()));
                        dt.setEtat(e);
                        dt.setUtilisateur(u);
                        if (typeDs.equalsIgnoreCase("deposit")) {
                            Type t = getNgContext().firstOrNull(Type.class, "id_type = ?", 1);
                            dt.setType(t);
                        } else {
                            Type t = getNgContext().firstOrNull(Type.class, "id_type = ?", 2);
                            dt.setType(t);
                        }
                        dt.setValeur(Double.parseDouble(valeur));
                        dt.setKey(id);
                        getNgContext().save(dt);
                    } else if (entite.equalsIgnoreCase("Favori")) {
                        DocumentSnapshot ds = getDocument(id, "Favoris", db);

                        String email = ds.getString("email");
                        Utilisateur u = getNgContext().firstOrNull(Utilisateur.class, "email = ?", email);

                        int idCrypto = ds.getLong("id_crypto").intValue();

                        CryptoFavori cf = new CryptoFavori();
                        cf.setKey(id);
                        cf.setId_crypto(idCrypto);
                        cf.setId_utilisateur(u.getId_utilisateur());

                        getNgContext().save(cf);

                    } else {
                        throw new Exception("Not supported entity : " + entite);
                    }
                }

                else if (type.equalsIgnoreCase("update")) {
                    if (entite.equalsIgnoreCase("Demande")) {
                        DocumentSnapshot ds = getDocument(id, "Demandes", db);

                        String etat = ds.getString("etat");
                        Etat e = getNgContext().firstOrNull(Etat.class, "designation = ?", etat);

                        DemandeTransaction dt = getNgContext().firstOrNull(DemandeTransaction.class, "key = ?", id);
                        dt.setEtat(e);

                        getNgContext().update(dt);
                    } else if (entite.equalsIgnoreCase("Utilisateur")) {
                        DocumentSnapshot ds = getDocument(id, "Utilisateurs", db);

                        String url = ds.getString("imageUrl");

                        Utilisateur u = getNgContext().findById(Integer.parseInt(id), Utilisateur.class);
                        u.setImageUrl(url);

                        getNgContext().update(u);
                    }
                } else if (type.equalsIgnoreCase("delete")) {
                    if (entite.equalsIgnoreCase("Favori")) {
                        getNgContext().executeUpdate("delete from crypto_favori where key = ?", id);
                    }
                }

                else {
                    throw new Exception("Not supported type : " + type);
                }

                db.collection("Synchro").document(doc.getId()).delete();
            }

            System.out.println("Sync database done");
        } catch (Exception e) {
            System.err.println("Error sync : " + e.getMessage());
        }
    }

    public static void updateFirebaseWallet(TransactionCrypto transaction, String email) throws Exception {
        Crypto crypto = transaction.getCrypto();
        double quantite = transaction.getQtty();
        String type = transaction.getType().getEtat();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference walletsCollection = db.collection("Wallets");

        Query query = walletsCollection
                .whereEqualTo("email", email)
                .whereEqualTo("crypto.id_crypto", crypto.getId_crypto()); // Vérifier si la crypto est bien un objet dans Firestore
        ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshotFuture.get().getDocuments();

        if (!documents.isEmpty()) {
            QueryDocumentSnapshot document = documents.get(0);
            Double currentQuantity = document.getDouble("quantite");
            if (currentQuantity == null) {
                currentQuantity = 0.0;
            }

            double newQuantity;
            if ("up".equalsIgnoreCase(type)) {
                newQuantity = currentQuantity + quantite;
            } else if ("down".equalsIgnoreCase(type)) {
                newQuantity = currentQuantity - quantite;
            } else {
                throw new Exception("Type unknown : " + type);
            }

            if (newQuantity == 0) {
                ApiFuture<WriteResult> deleteResult = document.getReference().delete();
                deleteResult.get();
            } else {
                ApiFuture<WriteResult> writeResult = document.getReference().update(
                        "quantite", newQuantity,
                        "last_update", Timestamp.now()
                );
                writeResult.get();
            }
        } else {
            if ("down".equalsIgnoreCase(type)) {
                throw new Exception("Cannot insert down type in firebase for user"
                        + email + " and crypto " + crypto.getUnit_nom());
            }

            Map<String, Object> newWallet = new HashMap<>();
            newWallet.put("email", email);
            newWallet.put("crypto", crypto);
            newWallet.put("quantite", quantite);
            newWallet.put("last_update", Timestamp.now());

            ApiFuture<DocumentReference> addedDocRef = walletsCollection.add(newWallet);
            addedDocRef.get();
        }
    }

    public static void deleteTable(String tableName) {
        DatabaseReference tableRef = FirebaseDatabase.getInstance().getReference(tableName);

        // Supprime les données
        tableRef.removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.out.println("Erreur lors de la suppression : " + databaseError.getMessage());
            } else {
                System.out.println("Table '" + tableName + "' supprimée avec succès !");
            }
        });
    }

    public static void sendNotificationToSubscribedCrypto(TransactionCrypto trans, String trader, RowResult rs) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        while (rs.next()) {
            String email = rs.get("email").toString();
            alertTrader(db, trans.getCour(), trans.getCrypto(), trans.getType().getEtat(), email, trader);
        }
    }

    private static void alertTrader(Firestore db, double cour, Crypto c, String type, String email, String trader) {
        try {
            ApiFuture<QuerySnapshot> future = db.collection("userToken")
                    .whereEqualTo("email", email)
                    .get();

            QuerySnapshot querySnapshot = future.get();

            if (querySnapshot.isEmpty()) {
                System.out.println("No FCM Token for user : " + email);
                return;
            }

            List<String> tokens = new ArrayList<>();

            // Récupérer tous les tokens associés à l'email
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                String token = document.getString("fcmToken");
                if (token != null && !token.isEmpty()) {
                    tokens.add(token);
                }
            }

            if (tokens.isEmpty()) {
                System.out.println("No valid FCM Tokens for user: " + email);
                return;
            }

            // Envoyer une notification par token
            for (String token : tokens) {
                try {
                    Message message = Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle("🚀 ALERTE TRADING")
                                    .setBody("Un trader vient de passer à l'action sur " + c.getNom() + "! 🔥 Restez à l'affût, le marché bouge ⚡📈")
                                    .build())
                            .putData("category", "crypto")
                            .putData("type", type)
                            .putData("amount", Service.formatDouble(cour))
                            .putData("trader", trader)
                            .putData("crypto_nom", c.getNom())
                            .putData("crypto_unit", c.getUnit_nom())
                            .putData("crypto_url", c.getUrl())
                            .setToken(token) // Envoi à UN SEUL token à la fois
                            .build();

                    FirebaseMessaging.getInstance().send(message);
                    System.out.println("Notification envoyée avec succès au token : " + token);
                } catch (FirebaseMessagingException e) {
                    System.out.println("Erreur lors de l'envoi au token " + token + " : " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de l'envoi de la notification : " + e.getMessage());
        }
    }
}
