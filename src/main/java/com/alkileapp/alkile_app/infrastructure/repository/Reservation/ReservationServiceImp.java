
package com.alkileapp.alkile_app.infrastructure.repository.Reservation;

import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.domain.entities.Reservation;
import com.alkileapp.alkile_app.domain.entities.Reservation.ReservationStatus;
import com.alkileapp.alkile_app.domain.entities.Tool;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImp implements IReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationServiceImp(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Reservation save(Reservation reservation) {
        Tool tool = reservation.getTool();
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        if (tool == null || tool.getId() == null) {
            throw new RuntimeException("La herramienta no tiene un ID válido.");
        }

        List<Reservation> existingReservations = reservationRepository.findByToolId(tool.getId());
        for (Reservation existing : existingReservations) {
            if (!existing.getStatus().equals(Reservation.ReservationStatus.CANCELED)) {
                boolean overlaps = !endDate.isBefore(existing.getStartDate()) &&
                        !startDate.isAfter(existing.getEndDate());
                if (overlaps) {
                    throw new RuntimeException("La herramienta no está disponible en las fechas seleccionadas.");
                }
            }
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));
        if (!reservation.getStatus().equals(ReservationStatus.PENDING)) {
            throw new IllegalStateException("Solo se pueden cancelar reservas pendientes");
        }
        reservation.setStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }
}

