package com.crypto.crypt.controller;

import com.crypto.crypt.model.Cour;
import com.crypto.crypt.model.Crypto;
import com.crypto.crypt.service.CryptoService;
import org.entityframework.dev.ApiResponse;
import org.entityframework.http.TokenRequired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/crypto")
public class CryptoController {
    
    @TokenRequired
    @GetMapping
    public ResponseEntity<ApiResponse> getAllCrypto() {
        try (CryptoService cryptoService = new CryptoService()) {
            List<Crypto> files = cryptoService.getAllCrypto();
            ApiResponse response = new ApiResponse(true, files, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @GetMapping("/cour")
    public ResponseEntity<ApiResponse> dernierCour() {
        try (CryptoService cryptoService = new CryptoService()) {
            List<Cour> files = cryptoService.dernierCours();
            ApiResponse response = new ApiResponse(true, files, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @TokenRequired
    @GetMapping("/cour/{idCrypto}")
    public ResponseEntity<ApiResponse> data(@PathVariable int idCrypto, @RequestParam int limit) {
        try (CryptoService cryptoService = new CryptoService()) {
            Object files = cryptoService.getCryptoData(idCrypto, limit);
            ApiResponse response = new ApiResponse(true, files, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }
}
