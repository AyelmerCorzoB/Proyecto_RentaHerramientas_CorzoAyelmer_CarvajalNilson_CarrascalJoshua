package com.alkileapp.alkile_app.domain.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationDto(
        Long id,
        Long customerId,
        Long toolId,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        LocalDateTime creationDate
) {}