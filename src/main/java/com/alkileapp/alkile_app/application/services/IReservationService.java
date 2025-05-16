package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.Reservation;

import java.util.List;
import java.util.Optional;

public interface IReservationService {
    List<Reservation> findAll();
    Optional<Reservation> findById(Long id);
    Reservation save(Reservation reservation);
    void deleteById(Long id);
    boolean existsById(Long id);

}
