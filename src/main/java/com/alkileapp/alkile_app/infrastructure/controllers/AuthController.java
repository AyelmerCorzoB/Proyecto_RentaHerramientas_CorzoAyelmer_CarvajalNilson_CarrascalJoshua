package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.domain.dto.AuthRequest;
import com.alkileapp.alkile_app.domain.dto.AuthResponse;
import com.alkileapp.alkile_app.domain.dto.RegisterRequest;
import com.alkileapp.alkile_app.application.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
       AuthResponse response = authService.login(request.username(), request.password());
    return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}