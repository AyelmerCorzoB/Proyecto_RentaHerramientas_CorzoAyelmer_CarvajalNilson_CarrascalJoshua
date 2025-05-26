package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.Customer;
import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
    Optional<Customer> findByTaxId(String taxId);
    Optional<Customer> findByUserId(Long userId);
    Customer save(Customer customer);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByTaxId(String taxId);
    
}
