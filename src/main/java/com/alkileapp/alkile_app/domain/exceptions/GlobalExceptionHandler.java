package com.alkileapp.alkile_app.domain.exceptions;

import com.alkileapp.alkile_app.domain.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handlerGenericException(Exception ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            ex.getMessage(),
            ex.getClass().getSimpleName(),
            request.getRequestURL().toString(),
            request.getMethod()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    
    @ExceptionHandler(io.jsonwebtoken.security.SecurityException.class)
    public ResponseEntity<ApiError> handlerJwtException(io.jsonwebtoken.security.SecurityException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            "Error en el token JWT: " + ex.getMessage(),
            ex.getClass().getSimpleName(),
            request.getRequestURL().toString(),
            request.getMethod()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }
}