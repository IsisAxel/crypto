package com.crypto.crypt.controller;

import java.util.List;

import org.entityframework.dev.ApiResponse;
import org.entityframework.http.TokenRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.crypt.conf.JwtUtil;
import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.service.UserService;

@RestController
@RequestMapping("/crypto/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCrypto() {
        try (UserService uService = new UserService()) {
            String token = "";
            //int id = Integer.parseInt(jwtUtil.extractSubject(token));
            int id = 4;

            Utilisateur u = uService.getUserInfo(id);
            ApiResponse response = new ApiResponse(true, u, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }
}
