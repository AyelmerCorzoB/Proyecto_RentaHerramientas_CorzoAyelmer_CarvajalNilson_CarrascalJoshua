package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.IPaymentService;
import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.domain.dto.PaymentDto;
import com.alkileapp.alkile_app.domain.entities.Payment;
import com.alkileapp.alkile_app.domain.entities.Reservation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alkile/payments")
public class PaymentController {

    private final IPaymentService paymentService;
    private final IReservationService reservationService;

    public PaymentController(IPaymentService paymentService, IReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAll() {
        List<PaymentDto> payments = paymentService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody PaymentDto paymentDto) {
        Payment payment = convertToEntity(paymentDto);
        Payment saved = paymentService.save(payment);
        return ResponseEntity.ok(convertToDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(@PathVariable Long id, @RequestBody PaymentDto paymentDto) {
        if (!paymentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Payment payment = convertToEntity(paymentDto);
        payment.setId(id);
        Payment updated = paymentService.save(payment);
        return ResponseEntity.ok(convertToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!paymentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private PaymentDto convertToDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getReservation().getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getPaymentDate(),
                payment.getStatus().name()
        );
    }

    private Payment convertToEntity(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setId(dto.id());
        payment.setAmount(dto.amount());
        payment.setPaymentMethod(dto.paymentMethod());
        payment.setTransactionId(dto.transactionId());
        payment.setPaymentDate(dto.paymentDate());
        payment.setStatus(Payment.PaymentStatus.valueOf(dto.status()));
        Reservation reservation = reservationService.findById(dto.reservationId()).orElseThrow();
        payment.setReservation(reservation);
        return payment;
    }

}
