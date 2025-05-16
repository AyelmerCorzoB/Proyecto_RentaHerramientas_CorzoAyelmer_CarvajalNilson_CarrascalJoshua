package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.Payment;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    List<Payment> findAll();
    Optional<Payment> findById(Long id);
    Payment save(Payment payment);
    void deleteById(Long id);
    boolean existsById(Long id);
}
