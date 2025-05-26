package com.alkileapp.alkile_app.domain.dto;

public record AuthResponse(String token, String role, Long userId) {}