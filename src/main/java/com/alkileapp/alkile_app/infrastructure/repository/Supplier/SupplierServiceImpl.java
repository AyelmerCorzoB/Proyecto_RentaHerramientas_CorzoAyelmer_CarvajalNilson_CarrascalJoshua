package com.alkileapp.alkile_app.infrastructure.repository.Supplier;

import com.alkileapp.alkile_app.application.services.ISupplierService;
import com.alkileapp.alkile_app.domain.entities.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements ISupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findByTaxId(String taxId) {
        return supplierRepository.findByTaxId(taxId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findByUserId(Long userId) {
        return supplierRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return supplierRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTaxId(String taxId) {
        return supplierRepository.existsByTaxId(taxId);
    }
}