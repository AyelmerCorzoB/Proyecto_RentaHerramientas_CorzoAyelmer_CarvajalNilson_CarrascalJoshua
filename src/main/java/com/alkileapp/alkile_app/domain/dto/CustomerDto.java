package com.alkileapp.alkile_app.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDto(
    Long id,
    
    @Size(max = 13, message = "Tax ID must be at most 13 characters")
    String taxId,
    
    @NotNull(message = "User ID is required")
    Long userId
) {}