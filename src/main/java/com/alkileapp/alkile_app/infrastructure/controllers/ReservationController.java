package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.ICustomerService;
import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.application.services.IToolService;
import com.alkileapp.alkile_app.domain.dto.ReservationDto;
import com.alkileapp.alkile_app.domain.entities.Customer;
import com.alkileapp.alkile_app.domain.entities.Reservation;
import com.alkileapp.alkile_app.domain.entities.Tool;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/alkile/reservations")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final IReservationService reservationService;
    private final ICustomerService customerService;
    private final IToolService toolService;

    public ReservationController(IReservationService reservationService, ICustomerService customerService,
            IToolService toolService) {
        this.reservationService = reservationService;
        this.customerService = customerService;
        this.toolService = toolService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getReservations(
            @RequestHeader(value = "userId", required = false) String userIdStr,
            @RequestHeader(value = "role", required = false) String role) {

        List<Reservation> reservations;
        Long userId = null;

        try {
            userId = (userIdStr != null && !userIdStr.isEmpty()) ? Long.parseLong(userIdStr) : null;
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        if (role == null || userId == null) {
            reservations = reservationService.findAll();
        } else {
            switch (role.toUpperCase()) {
                case "ADMIN":
                    reservations = reservationService.findAll();
                    break;
                case "CUSTOMER":
                    reservations = reservationService.findByCustomerId(userId);
                    break;
                case "SUPPLIER":
                    reservations = reservationService.findBySupplierId(userId);
                    break;
                default:
                    return ResponseEntity.status(403).build();
            }
        }

        List<ReservationDto> dtos = reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getById(@PathVariable Long id) {
        return reservationService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReservationDto reservationDto) {
        try {
            log.info("Datos recibidos: {}", reservationDto);

            // Validación adicional
            if (reservationDto.customerId() == null || reservationDto.customerId() <= 0) {
                log.error("Customer ID inválido: {}", reservationDto.customerId());
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Customer ID is required and must be positive"));
            }

            if (reservationDto.toolId() == null || reservationDto.toolId() <= 0) {
                log.error("Tool ID inválido: {}", reservationDto.toolId());
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tool ID is required and must be positive"));
            }

            // Verificar que el customer existe
            if (!customerService.existsById(reservationDto.customerId())) {
                log.error("Customer no encontrado: {}", reservationDto.customerId());
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Customer not found"));
            }

            // Verificar que la tool existe
            if (!toolService.existsById(reservationDto.toolId())) {
                log.error("Tool no encontrada: {}", reservationDto.toolId());
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tool not found"));
            }

            log.info("Creando reserva para cliente: {}, herramienta: {}",
                    reservationDto.customerId(), reservationDto.toolId());

            Reservation reservation = convertToEntity(reservationDto);
            Reservation saved = reservationService.save(reservation);
            return ResponseEntity.ok(convertToDto(saved));

        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al crear reserva", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Internal server error"));
        }
    }

    private Reservation convertToEntity(ReservationDto dto) {
        // Validate IDs
        Objects.requireNonNull(dto.customerId(), "Customer ID cannot be null");
        Objects.requireNonNull(dto.toolId(), "Tool ID cannot be null");

        // Load full Customer entity
        Customer customer = customerService.findById(dto.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + dto.customerId()));

        // Load full Tool entity
        Tool tool = toolService.findById(dto.toolId())
                .orElseThrow(() -> new EntityNotFoundException("Tool not found with id: " + dto.toolId()));

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setTool(tool);
        reservation.setStartDate(dto.startDate());
        reservation.setEndDate(dto.endDate());

        // Handle status - default to PENDING if not provided
        if (dto.status() != null) {
            reservation.setStatus(Reservation.ReservationStatus.valueOf(dto.status()));
        } else {
            reservation.setStatus(Reservation.ReservationStatus.PENDING);
        }

        reservation.setCreationDate(LocalDateTime.now());
        return reservation;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> update(@PathVariable Long id, @RequestBody ReservationDto reservationDto) {
        if (!reservationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Reservation reservation = convertToEntity(reservationDto);
        reservation.setId(id);
        Reservation updated = reservationService.save(reservation);
        return ResponseEntity.ok(convertToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!reservationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancelReservation(@PathVariable Long id) {
        try {
            Reservation canceledReservation = reservationService.cancelReservation(id);
            return ResponseEntity.ok(convertToDto(canceledReservation));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("customer/{customerId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByCustomer(@PathVariable Long customerId) {
        List<Reservation> reservations = reservationService.findByCustomerId(customerId);
        List<ReservationDto> dtos = reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("supplier/{supplierId}")
    public ResponseEntity<List<ReservationDto>> getReservationsBySupplier(@PathVariable Long supplierId) {
        List<Reservation> reservations = reservationService.findBySupplierId(supplierId);
        List<ReservationDto> dtos = reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private ReservationDto convertToDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getCustomer().getId(),
                reservation.getTool().getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus().name(),
                reservation.getCreationDate());
    }
}
