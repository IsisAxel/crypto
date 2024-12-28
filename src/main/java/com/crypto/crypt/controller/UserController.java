package com.crypto.crypt.controller;

import org.entityframework.dev.ApiResponse;
import org.entityframework.http.TokenRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.crypt.conf.JwtUtil;
import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.model.dto.TransactionFondDTO;
import com.crypto.crypt.service.UserService;

@RestController
@RequestMapping("/crypto/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    //@TokenRequired
    @GetMapping
    public ResponseEntity<ApiResponse> getUserInfo() {
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


    //@TokenRequired
    @GetMapping("/vdrequest")
    public ResponseEntity<ApiResponse> requestValidation() {
        try (UserService uService = new UserService()) {
            String token = "";
            //int id = Integer.parseInt(jwtUtil.extractSubject(token));
            int id = 4;

            uService.requestValidation(id);
            ApiResponse response = new ApiResponse(true, "", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @PostMapping("/{type}")
    public ResponseEntity<ApiResponse> transactionFond(@PathVariable String type, @RequestBody TransactionFondDTO data) {
        try (UserService uService = new UserService()) {
            uService.transaction(type, data);
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }
}
