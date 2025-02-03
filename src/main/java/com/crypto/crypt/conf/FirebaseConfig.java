package com.crypto.crypt.conf;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase.json");

            if (serviceAccount == null) {
                throw new IOException("Fichier firebase.json introuvable dans le dossier resources");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://fireproject-fff3b-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
