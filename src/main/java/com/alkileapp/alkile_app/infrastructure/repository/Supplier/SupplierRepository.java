package com.alkileapp.alkile_app.infrastructure.repository.Supplier;

import com.alkileapp.alkile_app.domain.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByTaxId(String taxId);
    Optional<Supplier> findByUserId(Long userId);
    boolean existsByTaxId(String taxId);
}