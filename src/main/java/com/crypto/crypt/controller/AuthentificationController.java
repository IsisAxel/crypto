package com.crypto.crypt.controller;

import org.entityframework.dev.ApiResponse;
import org.entityframework.http.TokenRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.crypto.crypt.model.dto.ResponseApi;
import com.crypto.crypt.service.IdentityProviderService;

import java.util.Map;

@RestController
@RequestMapping("/crypto/auth")
public class AuthentificationController {
    // private static final String AUTH_URL = "http://fast_auth:5000";
    private static final String AUTH_URL = "http://localhost:5000";

    @Autowired
    private IdentityProviderService identityProviderService;

    @TokenRequired
    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken() {
        ApiResponse apiResponse = new ApiResponse(true, null, "valide");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> requestBody) {
        String identityProviderUrl = AUTH_URL + "/api/User/register"; 
        return identityProviderService.directCall(identityProviderUrl, HttpMethod.POST, requestBody);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateEmail(@RequestParam String key) {
        String identityProviderUrl = AUTH_URL + "/api/User/confirm?key=" + key; 
        
        try {
            ResponseEntity<?> rp = identityProviderService.directCall(identityProviderUrl, HttpMethod.POST);
            ResponseEntity<?> result = identityProviderService.extractAndPersistUser(rp);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            ResponseApi response = new ResponseApi();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(500);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> requestBody) {
        String identityProviderUrl = AUTH_URL + "/api/User/login"; 
        return identityProviderService.directCall(identityProviderUrl, HttpMethod.POST, requestBody);
    }

    @PostMapping("/confirmPin")
    public ResponseEntity<?> confirmPin(@RequestBody Map<String, Object> requestBody) {
        String identityProviderUrl = AUTH_URL + "/api/User/auth"; 
        try {
            ResponseEntity<?> rp = identityProviderService.directCall(identityProviderUrl, HttpMethod.POST, requestBody);
            ResponseEntity<?> result = identityProviderService.extractUser(rp);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            ResponseApi response = new ResponseApi();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(500);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
        }
    }

    @PostMapping("/reset/request")
    public ResponseEntity<?> requestReset(@RequestBody Map<String, Object> requestBody) {
        String identityProviderUrl = AUTH_URL + "/api/User/sendReset"; 
        return identityProviderService.directCall(identityProviderUrl, HttpMethod.POST, requestBody);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestParam String key) {
        String identityProviderUrl = AUTH_URL + "/api/User/reset?key=" + key; 
        return identityProviderService.directCall(identityProviderUrl, HttpMethod.POST);
    }
}
