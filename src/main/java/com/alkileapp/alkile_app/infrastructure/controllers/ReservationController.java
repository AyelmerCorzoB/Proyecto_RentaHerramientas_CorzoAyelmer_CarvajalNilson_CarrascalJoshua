package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.ICustomerService;
import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.application.services.IToolService;
import com.alkileapp.alkile_app.domain.dto.ReservationDto;
import com.alkileapp.alkile_app.domain.entities.Customer;
import com.alkileapp.alkile_app.domain.entities.Reservation;
import com.alkileapp.alkile_app.domain.entities.Tool;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alkile/reservations")
public class ReservationController {

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
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestHeader(value = "role", required = false) String role) {
        
        List<Reservation> reservations;

        // Si no se proporcionan headers, devolver todas las reservaciones (para compatibilidad)
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
    public ResponseEntity<ReservationDto> create(@RequestBody ReservationDto reservationDto) {
        Reservation reservation = convertToEntity(reservationDto);
        Reservation saved = reservationService.save(reservation);
        return ResponseEntity.ok(convertToDto(saved));
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

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByCustomer(@PathVariable Long customerId) {
        List<Reservation> reservations = reservationService.findByCustomerId(customerId);
        List<ReservationDto> dtos = reservations.stream()
                .map(this::convertToDto)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ReservationDto>> getReservationsBySupplier(@PathVariable Long supplierId) {
        List<Reservation> reservations = reservationService.findBySupplierId(supplierId);
        List<ReservationDto> dtos = reservations.stream()
                .map(this::convertToDto)
                .collect(java.util.stream.Collectors.toList());
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

    private Reservation convertToEntity(ReservationDto dto) {
        Reservation reservation = new Reservation();
        reservation.setId(dto.id());
        reservation.setStartDate(dto.startDate());
        reservation.setEndDate(dto.endDate());
        reservation.setStatus(Reservation.ReservationStatus.valueOf(dto.status()));
        reservation.setCreationDate(dto.creationDate());
        Customer customer = customerService.findById(dto.customerId()).orElseThrow();
        Tool tool = toolService.findById(dto.toolId()).orElseThrow();
        reservation.setCustomer(customer);
        reservation.setTool(tool);
        return reservation;
    }
}