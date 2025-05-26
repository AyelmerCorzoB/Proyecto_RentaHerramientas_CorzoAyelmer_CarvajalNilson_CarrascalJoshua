package com.alkileapp.alkile_app.infrastructure.repository.Customer;

import com.alkileapp.alkile_app.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByTaxId(String taxId);

    @Query("SELECT c FROM Customer c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Customer> findByIdWithUser(@Param("id") Long id);

    boolean existsByTaxId(String taxId);
}