package com.alkileapp.alkile_app.domain.dto;

import java.time.LocalDateTime;

public record PaymentDto(
        Long id,
        Long reservationId,
        double amount,
        String paymentMethod,
        String transactionId,
        LocalDateTime paymentDate,
        String status
) {}