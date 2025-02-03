package com.crypto.crypt.socket;

import com.crypto.crypt.model.Cour;
import com.crypto.crypt.model.CourLine;
import com.crypto.crypt.service.CryptoService;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SocketService {

    @Autowired
    private CryptoWebSocketHandler webSocketHandler;
    private final CryptoService repository = new CryptoService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Cour> derniersCours = new ArrayList<>(); 

    @Scheduled(fixedRate = 200000)
    public void generateAndBroadcastCours() {
        try {
            repository.generateCours();
            List<Cour> nouveauxCours = repository.dernierCours();
            List<CourLine> courLines = new ArrayList<>();

            for (int i = 0; i < nouveauxCours.size(); i++) {
                Cour dernierCour = nouveauxCours.get(i);
                CourLine courLine = new CourLine();
                courLine.setCrypto(dernierCour.getCrypto());
                courLine.setValeur(dernierCour.getValeur());
                courLine.setDate_changement(dernierCour.getDate_changement());

                if (!derniersCours.isEmpty() && i < derniersCours.size()) {
                    Cour precedentCour = derniersCours.get(i);
                    double variation = calculateVariation(precedentCour.getValeur(), dernierCour.getValeur());
                    courLine.setVariation(variation);
                } else {
                    courLine.setVariation(0.0); 
                }

                courLines.add(courLine);
            }

            derniersCours = nouveauxCours;

            String message = objectMapper.writeValueAsString(courLines);
            System.out.println("Generating");
            webSocketHandler.sendMessage(message);

            updateFirebaseDatabase(courLines);

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private double calculateVariation(double precedentValeur, double dernierValeur) {
        return ((dernierValeur - precedentValeur) / precedentValeur) * 100;
    }

    private void updateFirebaseDatabase(List<CourLine> courLines) {
        try {
            if (!com.crypto.crypt.service.Service.isOnlineMode()) {
                System.out.println("No internet Connection. Cannot update Firebase RealTime Database");
                return;
            }

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("CourLines");
            List<Map<String, Object>> courLinesData = new ArrayList<>();

            for (CourLine courLine : courLines) {
                Map<String, Object> courLineMap = new HashMap<>();
                courLineMap.put("crypto", courLine.getCrypto());
                courLineMap.put("valeur", courLine.getValeur());
                courLineMap.put("variation", courLine.getVariation());

                courLinesData.add(courLineMap);
            }

            dbRef.setValue(courLinesData, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    System.out.println("Error : " + databaseError.getMessage());
                } else {
                    System.out.println("Firebase Realtime updated");
                }
            });

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}