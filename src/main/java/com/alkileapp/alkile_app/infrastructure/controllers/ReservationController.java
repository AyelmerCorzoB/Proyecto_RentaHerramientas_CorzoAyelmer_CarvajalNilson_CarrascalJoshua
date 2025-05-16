package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.ICustomerService;
import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.application.services.IToolService;
import com.alkileapp.alkile_app.domain.dto.ReservationDto;
import com.alkileapp.alkile_app.domain.entities.Customer;
import com.alkileapp.alkile_app.domain.entities.Reservation;
import com.alkileapp.alkile_app.domain.entities.Tool;
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

    public ReservationController(IReservationService reservationService, ICustomerService customerService, IToolService toolService) {
        this.reservationService = reservationService;
        this.customerService = customerService;
        this.toolService = toolService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAll() {
        List<ReservationDto> reservations = reservationService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
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

    private ReservationDto convertToDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getCustomer().getId(),
                reservation.getTool().getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus().name(),
                reservation.getCreationDate()
        );
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
