package com.alkileapp.alkile_app.infrastructure.repository.Customer;

import com.alkileapp.alkile_app.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByTaxId(String taxId);
    Optional<Customer> findByUserId(Long userId);
    boolean existsByTaxId(String taxId);
}