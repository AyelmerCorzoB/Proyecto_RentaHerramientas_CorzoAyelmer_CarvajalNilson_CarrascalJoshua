package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.Supplier;
import java.util.List;
import java.util.Optional;

public interface ISupplierService {
    List<Supplier> findAll();
    Optional<Supplier> findById(Long id);
    Optional<Supplier> findByTaxId(String taxId);
    Optional<Supplier> findByUserId(Long userId);
    Supplier save(Supplier supplier);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByTaxId(String taxId);
}