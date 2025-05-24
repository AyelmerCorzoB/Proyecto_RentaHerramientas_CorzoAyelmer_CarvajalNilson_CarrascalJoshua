package com.alkileapp.alkile_app.infrastructure.repository.Invoice;

import com.alkileapp.alkile_app.application.services.IInvoiceService;
import com.alkileapp.alkile_app.domain.entities.Invoice;
import com.alkileapp.alkile_app.infrastructure.repository.Invoice.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImp implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImp(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
}