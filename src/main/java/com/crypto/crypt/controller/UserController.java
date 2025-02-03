package com.crypto.crypt.controller;

import com.crypto.crypt.model.Operation;
import org.entityframework.dev.ApiResponse;
import org.entityframework.http.TokenRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getUsers() {
        try (UserService uService = new UserService()) {
            Object data = uService.getUtilisateurs();
            ApiResponse response = new ApiResponse(true, data, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/depot")
    public ResponseEntity<ApiResponse> depot(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionFondDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));

            uService.beginTransaction();
            uService.demandeTransaction(id, "depot", data);

            uService.endTransaction();
            uService.commit();
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/retrait")
    public ResponseEntity<ApiResponse> retrait(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionFondDTO data) {
        try (UserService uService = new UserService()) {
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            uService.beginTransaction();

            uService.demandeTransaction(id, "retrait", data);

            uService.endTransaction();
            uService.commit();

            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/buy")
    public ResponseEntity<ApiResponse> buyCrypto(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionCryptoDTO data) {
        try (UserService uService = new UserService()) {
            uService.beginTransaction();
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            uService.buyCrypto(data, id);


            uService.endTransaction();
            uService.commit();
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @PostMapping("/sell")
    public ResponseEntity<ApiResponse> sellCrypto(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TransactionCryptoDTO data) {
        try (UserService uService = new UserService()) {
            uService.beginTransaction();
            String token = authorizationHeader.replace("Bearer ", "");
            int id = Integer.parseInt(jwtUtil.extractSubject(token));
            uService.sellCrypto(data, id);

            uService.endTransaction();
            uService.commit();
            ApiResponse response = new ApiResponse(true, "reussi", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @GetMapping("/operations")
    public ResponseEntity<ApiResponse> getOperations(@RequestParam(value = "idu", required = false, defaultValue = "0") int idu, @RequestParam(value = "idc", required = false, defaultValue = "0") int idc) {
        try (UserService uService = new UserService()) {
            Object data = uService.getAllOperations(idu, idc);
            ApiResponse response = new ApiResponse(true, data, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse> getTransactions(@RequestParam(value = "idu", required = false, defaultValue = "0") int idu, @RequestParam(value = "idc", required = false, defaultValue = "0") int idc) {
        try (UserService uService = new UserService()) {
            Object data = uService.getTransactions(idu, idc);
            ApiResponse response = new ApiResponse(true, data, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }
}
