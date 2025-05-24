package com.alkileapp.alkile_app.domain.dto;

import java.time.LocalDateTime;

public record DamageReportDto(
        Long id,
        Long reservationId,
        String description,
        double repairCost,
        LocalDateTime reportDate,
        boolean resolved
) {}