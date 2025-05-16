package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.ICustomerService;
import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.dto.CustomerDto;
import com.alkileapp.alkile_app.domain.entities.Customer;
import com.alkileapp.alkile_app.domain.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final ICustomerService customerService;
    private final IUserService userService;

    public CustomerController(ICustomerService customerService, IUserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDTO) {
        if (customerDTO.id() != null && customerService.existsById(customerDTO.id())) {
            return ResponseEntity.badRequest().build();
        }
        
        if (customerDTO.taxId() != null && customerService.existsByTaxId(customerDTO.taxId())) {
            return ResponseEntity.badRequest().build();
        }
        
        Optional<User> userOptional = userService.findById(customerDTO.userId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Customer customer = convertToEntity(customerDTO, userOptional.get());
        Customer savedCustomer = customerService.save(customer);
        return new ResponseEntity<>(convertToDTO(savedCustomer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDTO) {
        if (!customerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<User> userOptional = userService.findById(customerDTO.userId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Customer customer = convertToEntity(customerDTO, userOptional.get());
        customer.setId(id);
        Customer updatedCustomer = customerService.save(customer);
        return ResponseEntity.ok(convertToDTO(updatedCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!customerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CustomerDto convertToDTO(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getTaxId(),
                customer.getUser().getId()
        );
    }

    private Customer convertToEntity(CustomerDto dto, User user) {
        Customer customer = new Customer();
        customer.setTaxId(dto.taxId());
        customer.setUser(user);
        return customer;
    }
}