package com.alkileapp.alkile_app.infrastructure.repository.Invoice;

import com.alkileapp.alkile_app.domain.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}