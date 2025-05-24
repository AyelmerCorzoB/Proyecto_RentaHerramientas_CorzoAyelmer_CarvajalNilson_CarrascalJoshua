package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.ISupplierService;
import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.dto.SupplierDto;
import com.alkileapp.alkile_app.domain.entities.Supplier;
import com.alkileapp.alkile_app.domain.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alkile/suppliers")
public class SupplierController {

    private final ISupplierService supplierService;
    private final IUserService userService;

    public SupplierController(ISupplierService supplierService, IUserService userService) {
        this.supplierService = supplierService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        List<SupplierDto> suppliers = supplierService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable Long id) {
        return supplierService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SupplierDto> createSupplier(@Valid @RequestBody SupplierDto supplierDto) {
        if (supplierDto.id() != null && supplierService.existsById(supplierDto.id())) {
            return ResponseEntity.badRequest().build();
        }
        
        if (supplierService.existsByTaxId(supplierDto.taxId())) {
            return ResponseEntity.badRequest().build();
        }
        
        Optional<User> userOptional = userService.findById(supplierDto.userId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Supplier supplier = convertToEntity(supplierDto, userOptional.get());
        Supplier savedSupplier = supplierService.save(supplier);
        return new ResponseEntity<>(convertToDTO(savedSupplier), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierDto supplierDto) {
        if (!supplierService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Optional<User> userOptional = userService.findById(supplierDto.userId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Supplier supplier = convertToEntity(supplierDto, userOptional.get());
        supplier.setId(id);
        Supplier updatedSupplier = supplierService.save(supplier);
        return ResponseEntity.ok(convertToDTO(updatedSupplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        if (!supplierService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        supplierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private SupplierDto convertToDTO(Supplier supplier) {
        return new SupplierDto(
                supplier.getId(),
                supplier.getTaxId(),
                supplier.getCompany(),
                supplier.getRating(),
                supplier.getUser().getId()
        );
    }

    private Supplier convertToEntity(SupplierDto dto, User user) {
        Supplier supplier = new Supplier();
        supplier.setTaxId(dto.taxId());
        supplier.setCompany(dto.company());
        supplier.setRating(dto.rating());
        supplier.setUser(user);
        return supplier;
    }
}