package com.crypto.crypt.service;

import java.sql.Timestamp;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.crypto.crypt.conf.JwtUtil;
import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.model.tiers.SessionUser;

@Component
public class IdentityProviderService {

    private final JwtUtil jwtUtil;

    public IdentityProviderService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> directCall(String url, HttpMethod method, Map<String, Object> requestBody) {
        String identityProviderUrl = url;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                identityProviderUrl,
                method,
                requestEntity,
                String.class
            );

            return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody() != null ? response.getBody() : Map.of("message", "Pas de contenu"));
        } catch (HttpClientErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();

            if (responseBody == null || responseBody.isBlank()) {
                responseBody = "{\"error\":\"Erreur dans la communication avec le fournisseur d'identité\"}";
            }

            return ResponseEntity
                .status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
        }
    }

    public ResponseEntity<?> directCall(String url, HttpMethod method) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                method,
                requestEntity,
                String.class
            );
    
            return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody() != null ? response.getBody() : Map.of("message", "Pas de contenu"));
        } catch (HttpClientErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();
    
            if (responseBody == null || responseBody.isBlank()) {
                responseBody = "{\"error\":\"Erreur dans la communication avec l'API\"}";
            }
    
            return ResponseEntity
                .status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
        }
    }  
    
    public ResponseEntity<?> extractAndPersistUser(ResponseEntity<?> rp) throws Exception {
        String responseBody = (String) rp.getBody();
        JSONObject jsonResponse = new JSONObject(responseBody);
        boolean isSuccess = jsonResponse.getBoolean("isSuccess");

        if (isSuccess) {
            JSONObject dataObject = jsonResponse.optJSONObject("data");
            JSONObject userObject = dataObject.optJSONObject("user");

            int f_id = userObject.getInt("idUser");
            String nom = userObject.getString("username");
            String email = userObject.getString("email");

            Utilisateur user = new Utilisateur(f_id, nom, email);
            try (UserService userService = new UserService()) {
                userService.beginTransaction();

                int id_utilisateur = userService.saveUser(user);
                user.setId_utilisateur(id_utilisateur);

                String token = jwtUtil.generateToken(String.valueOf(id_utilisateur));
                String f_token = dataObject.getString("token");
                Timestamp expiration = jwtUtil.getInstantExpiration();

                SessionUser session = new SessionUser(token, f_token, expiration, id_utilisateur);
                userService.createSession(session);

                dataObject.put("token", token);

                new FirebaseService().saveUser(user);

                userService.commit();
                userService.endTransaction();

                return ResponseEntity
                .status(rp.getStatusCode()) 
                .contentType(MediaType.APPLICATION_JSON) 
                .body(jsonResponse.toString()); 
            }
        }

        return rp;
    }

    public ResponseEntity<?> extractUser(ResponseEntity<?> rp) throws Exception {
        String responseBody = (String) rp.getBody();

        JSONObject jsonResponse = new JSONObject(responseBody);
        boolean isSuccess = jsonResponse.getBoolean("isSuccess");

        if (isSuccess) {
            JSONObject dataObject = jsonResponse.optJSONObject("data");
            JSONObject userObject = dataObject.optJSONObject("user");

            int f_id = userObject.getInt("idUser");
            try (UserService userService = new UserService()) {
                Utilisateur user = userService.findUtilisateurF(f_id);
                int id_utilisateur = user.getId_utilisateur();

                String token = jwtUtil.generateToken(String.valueOf(id_utilisateur));
                String f_token = dataObject.getString("token");
                Timestamp expiration = jwtUtil.getInstantExpiration();

                SessionUser session = new SessionUser(token, f_token, expiration, id_utilisateur);
                userService.createSession(session);

                dataObject.put("token", token);

                return ResponseEntity
                .status(rp.getStatusCode()) 
                .contentType(MediaType.APPLICATION_JSON) 
                .body(jsonResponse.toString()); 
            }
        }

        return rp;
    }
}
