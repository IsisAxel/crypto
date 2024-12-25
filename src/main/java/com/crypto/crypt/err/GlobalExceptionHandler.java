package com.crypto.crypt.err;

import org.entityframework.dev.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ApiResponse> handleTokenValidationException(TokenValidationException ex) {
        return ResponseEntity.internalServerError().body(ApiResponse.Of(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body(ApiResponse.Of(ex));

    }
}
