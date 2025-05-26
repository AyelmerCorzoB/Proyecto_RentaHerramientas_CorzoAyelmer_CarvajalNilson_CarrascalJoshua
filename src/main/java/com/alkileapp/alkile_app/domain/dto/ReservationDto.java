package com.alkileapp.alkile_app.domain.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationDto(
        Long id,
        
        @NotNull(message = "Customer ID is required")
        Long customerId,
        
        @NotNull(message = "Tool ID is required")    
        Long toolId,
        
        @NotNull(message = "Start date is required")
        LocalDate startDate,
        
        @NotNull(message = "End date is required")
        LocalDate endDate,
        
        String status,
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime creationDate
) {
    public ReservationDto {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (toolId == null) {
            throw new IllegalArgumentException("Tool ID cannot be null");
        }
        // Validar que la fecha de fin sea después de la fecha de inicio
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
