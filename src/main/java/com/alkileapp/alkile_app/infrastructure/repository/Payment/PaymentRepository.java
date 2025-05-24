package com.alkileapp.alkile_app.infrastructure.repository.Payment;

import com.alkileapp.alkile_app.domain.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
