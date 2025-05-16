package com.alkileapp.alkile_app.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SupplierDto(
    Long id,
    
    @NotBlank(message = "Tax ID is required")
    @Size(max = 13, message = "Tax ID must be at most 13 characters")
    String taxId,
    
    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name must be at most 100 characters")
    String company,
    
    Double rating,
    
    @NotNull(message = "User ID is required")
    Long userId
) {}