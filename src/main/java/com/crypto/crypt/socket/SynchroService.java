package com.crypto.crypt.socket;

import com.crypto.crypt.service.FirebaseService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SynchroService {

    private final FirebaseService service = new FirebaseService(true);

    @Scheduled(fixedRate = 5000)
    public void sync() {
        //System.out.println("Sync bro");
        service.synchroniser();
    }
}
