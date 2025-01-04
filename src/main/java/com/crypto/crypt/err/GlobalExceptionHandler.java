package com.crypto.crypt.err;

import org.entityframework.dev.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ApiResponse> handleTokenValidationException(TokenValidationException ex) {
        ApiResponse api = new ApiResponse(false, false, "false");
        return ResponseEntity.internalServerError().body(api);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.internalServerError().body(ApiResponse.Of(ex));
    }
}
