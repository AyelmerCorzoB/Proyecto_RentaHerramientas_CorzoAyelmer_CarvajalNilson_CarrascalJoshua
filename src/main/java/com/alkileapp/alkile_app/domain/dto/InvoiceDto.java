package com.alkileapp.alkile_app.domain.dto;

import java.time.LocalDateTime;

public record InvoiceDto(
        Long id,
        Long paymentId,
        LocalDateTime issueDate,
        String details) {
}
