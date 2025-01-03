package com.crypto.crypt.controller;

import org.entityframework.dev.ApiResponse;
import org.entityframework.http.TokenRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.crypt.conf.JwtUtil;
import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.model.dto.FeedbackDTO;
import com.crypto.crypt.model.dto.TransactionCryptoDTO;
import com.crypto.crypt.model.dto.TransactionFondDTO;
import com.crypto.crypt.model.tiers.Portefeuille;
import com.crypto.crypt.service.UserService;

@RestController
@RequestMapping("/crypto/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @TokenRequired
    @GetMapping
     public ResponseEntity<ApiResponse> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));

            Utilisateur u = uService.getUserInfo(id);
            ApiResponse response = new ApiResponse(true, u, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @GetMapping("/sm")
     public ResponseEntity<ApiResponse> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));

            Utilisateur u = uService.getUser(id);
            ApiResponse response = new ApiResponse(true, u, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @GetMapping("/vdrequest")
    public ResponseEntity<ApiResponse> requestValidation(@RequestHeader("Authorization") String authorizationHeader) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));

            uService.requestValidation(id);
            ApiResponse response = new ApiResponse(true, "", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/{type}")
    public ResponseEntity<ApiResponse> transactionFond(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String type, @RequestBody TransactionFondDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            
            uService.transaction(id, type, data);
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/depot")
    public ResponseEntity<ApiResponse> depot(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionFondDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            
            uService.transaction(id, "depot", data);
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/retrait")
    public ResponseEntity<ApiResponse> retrait(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionFondDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            
            uService.transaction(id, "retrait", data);
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/buy")
    public ResponseEntity<ApiResponse> buyCrypto(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionCryptoDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            uService.buyCrypto(data, id);
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/sell")
    public ResponseEntity<ApiResponse> sellCrypto(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionCryptoDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            uService.sellCrypto(data, id);
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/feedback")
    public ResponseEntity<ApiResponse> feedback(@RequestHeader("Authorization") String authorizationHeader, @RequestBody FeedbackDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            uService.feedback(data, id);
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));

            uService.logout(token, id);
            ApiResponse response = new ApiResponse(true, "", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @GetMapping("/wallet/{idCrypto}")
    public ResponseEntity<ApiResponse> getWallet(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int idCrypto) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));

            Portefeuille p = uService.getUserWallet(id, idCrypto);
            ApiResponse response = new ApiResponse(true, p, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }
}
