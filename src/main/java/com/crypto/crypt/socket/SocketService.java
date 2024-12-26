package com.crypto.crypt.socket;

import com.crypto.crypt.model.Cour;
import com.crypto.crypt.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Service
public class SocketService {

    @Autowired
    private CryptoWebSocketHandler webSocketHandler;
    private final CryptoService repository = new CryptoService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 10000)
    public void generateAndBroadcastCours() {
        try {
            repository.generateCours(30000, 60000); 
            List<Cour> derniersCours = repository.dernierCours();
            String message = objectMapper.writeValueAsString(derniersCours);

            webSocketHandler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

