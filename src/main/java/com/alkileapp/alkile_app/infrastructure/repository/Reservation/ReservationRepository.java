package com.alkileapp.alkile_app.infrastructure.repository.Reservation;

import com.alkileapp.alkile_app.domain.entities.Reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByToolId(Long toolId);

}
