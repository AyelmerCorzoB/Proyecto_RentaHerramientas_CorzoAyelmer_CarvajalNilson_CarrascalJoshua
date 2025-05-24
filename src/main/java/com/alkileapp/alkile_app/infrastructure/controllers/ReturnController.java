package com.alkileapp.alkile_app.infrastructure.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.application.services.IReturnService;
import com.alkileapp.alkile_app.domain.dto.ReturnDto;
import com.alkileapp.alkile_app.domain.entities.Reservation;
import com.alkileapp.alkile_app.domain.entities.Return;

@RestController
@RequestMapping("/api/alkile/returns")
public class ReturnController {
    private final IReturnService returnService;
    private final IReservationService reservationService;

    public ReturnController(IReturnService returnService, IReservationService reservationService) {
        this.returnService = returnService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReturnDto> createReturn(@RequestBody ReturnDto dto) {
        Return r = new Return();
        r.setId(dto.id());
        r.setComments(dto.comments());
        r.setReturnDate(dto.returnDate());

        Reservation reservation = reservationService.findById(dto.reservationId()).orElseThrow();
        r.setReservation(reservation);

        Return saved = returnService.save(r);

        // Actualizar estado de la reserva
        reservation.setStatus(Reservation.ReservationStatus.COMPLETED);
        reservationService.save(reservation);

        return ResponseEntity.ok(new ReturnDto(
            saved.getId(),
            saved.getReservation().getId(),
            saved.getReturnDate(),
            saved.getComments()
        ));
    }
}
