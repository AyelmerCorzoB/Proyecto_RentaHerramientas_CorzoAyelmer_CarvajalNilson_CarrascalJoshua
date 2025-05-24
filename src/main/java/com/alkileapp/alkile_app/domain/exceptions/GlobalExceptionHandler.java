package com.alkileapp.alkile_app.domain.exceptions;

import com.alkileapp.alkile_app.domain.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            ex.getMessage(),
            ex.getClass().getSimpleName(),
            request.getRequestURL().toString(),
            request.getMethod()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    
    @ExceptionHandler(io.jsonwebtoken.security.SecurityException.class)
    public ResponseEntity<ApiError> handleJwtException(io.jsonwebtoken.security.SecurityException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            "Error en el token JWT: " + ex.getMessage(),
            ex.getClass().getSimpleName(),
            request.getRequestURL().toString(),
            request.getMethod()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            "Validation error: " + ex.getMessage(),
            ex.getClass().getSimpleName(),
            request.getRequestURL().toString(),
            request.getMethod()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
            "Access denied: " + ex.getMessage(),
            ex.getClass().getSimpleName(),
            request.getRequestURL().toString(),
            request.getMethod()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }
}