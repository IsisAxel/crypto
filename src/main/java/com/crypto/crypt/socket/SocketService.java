package com.crypto.crypt.socket;

import com.crypto.crypt.model.Cour;
import com.crypto.crypt.model.CourLine;
import com.crypto.crypt.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class SocketService {

    @Autowired
    private CryptoWebSocketHandler webSocketHandler;
    private final CryptoService repository = new CryptoService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Cour> derniersCours = new ArrayList<>(); 

    @Scheduled(fixedRate = 1000)
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
            System.out.println("Generating CourLine with variation...");
            webSocketHandler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateVariation(double precedentValeur, double dernierValeur) {
        return ((dernierValeur - precedentValeur) / precedentValeur) * 100;
    }
}


