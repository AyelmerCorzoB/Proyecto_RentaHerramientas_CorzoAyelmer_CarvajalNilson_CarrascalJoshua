package com.alkileapp.alkile_app.infrastructure.repository.Customer;

import com.alkileapp.alkile_app.application.services.ICustomerService;
import com.alkileapp.alkile_app.domain.entities.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // CORRECCIÓN: Agregar transaccional a nivel de clase
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findByTaxId(String taxId) {
        return customerRepository.findByTaxId(taxId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findByUserId(Long userId) {
        // CORRECCIÓN: Usar método correcto
        return customerRepository.findById(userId);
    }

    @Override
    public Customer save(Customer customer) {
        // CORRECCIÓN: Validar antes de guardar
        if (customer.getUser() == null) {
            throw new IllegalArgumentException("Customer must have a user");
        }
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTaxId(String taxId) {
        return customerRepository.existsByTaxId(taxId);
    }
}
