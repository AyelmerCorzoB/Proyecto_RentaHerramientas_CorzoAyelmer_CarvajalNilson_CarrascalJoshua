package com.alkileapp.alkile_app.domain.dto;

import java.time.LocalDateTime;

public record ReturnDto(
    Long id,
    Long reservationId,
    LocalDateTime returnDate,
    String comments
) {}
