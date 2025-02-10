package com.crypto.crypt.controller;

import com.crypto.crypt.model.Cour;
import com.crypto.crypt.model.Crypto;
import com.crypto.crypt.model.tiers.Commission;
import com.crypto.crypt.service.AdminService;
import com.crypto.crypt.service.CryptoService;
import com.crypto.crypt.service.FirebaseService;
import org.entityframework.dev.ApiResponse;
import org.entityframework.http.TokenRequired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @GetMapping("/commission")
    public ResponseEntity<ApiResponse> comm() {
        try (CryptoService cryptoService = new CryptoService()) {
            Object data = cryptoService.getCommission();
            ApiResponse response = new ApiResponse(true, data, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @PutMapping("/commission")
    public ResponseEntity<ApiResponse> modifCommission(@RequestBody Commission comm) {
        try (CryptoService cryptoService = new CryptoService()) {
            cryptoService.modifCommission(comm);
            ApiResponse response = new ApiResponse(true, "", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @GetMapping("/demande/list")
    public ResponseEntity<ApiResponse> listeDemande() {
        try (AdminService service = new AdminService()) {
            Object data = service.getDemandesAttente();
            ApiResponse response = new ApiResponse(true, data, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @PutMapping("/demande/valider/{id}")
    public ResponseEntity<ApiResponse> valider(@PathVariable int id) {
        try (AdminService service = new AdminService()) {
            service.beginTransaction();
            service.repondre(id, true);

            service.endTransaction();
            service.commit();
            ApiResponse response = new ApiResponse(true, "", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    //@TokenRequired
    @PutMapping("/demande/refuser/{id}")
    public ResponseEntity<ApiResponse> refuser(@PathVariable int id) {
        try (AdminService service = new AdminService()) {
            service.beginTransaction();

            service.repondre(id, false);

            service.endTransaction();
            service.commit();
            ApiResponse response = new ApiResponse(true, "", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }


    //@TokenRequired
    @PutMapping("/demande/annuler/{id}")
    public ResponseEntity<ApiResponse> annuler(@PathVariable int id) {
        try (AdminService service = new AdminService()) {
            service.beginTransaction();

            service.supprimerDemande(id);

            service.endTransaction();
            service.commit();
            ApiResponse response = new ApiResponse(true, "", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }

    @DeleteMapping("/firebase/tables/{tableName}")
    public ResponseEntity<ApiResponse> deleteTable(@PathVariable String tableName) {
        try {
            FirebaseService.deleteTable(tableName);
            ApiResponse response = new ApiResponse(true, true, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().body(ApiResponse.Of(e));
        }
    }
}
