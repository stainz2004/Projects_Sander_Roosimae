package org.example.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AddReservationDto;
import org.example.backend.entity.Reservation;
import org.example.backend.exception.ApplicationException;
import org.example.backend.repository.ReservationRepository;
import org.example.backend.repository.SeatingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatingRepository seatingRepository;

    @Transactional
    public void addReservation(AddReservationDto addReservationDto) {

        boolean hasOverlappingBooking = reservationRepository.existsBySeatingIdAndStartTimeBetween(
                addReservationDto.getSeatingId(),
                addReservationDto.getStartTime().minusHours(2).plusMinutes(1),
                addReservationDto.getStartTime().plusHours(2).minusMinutes(1)
        );

        if (hasOverlappingBooking) {
            throw new ApplicationException("Booking overlaps with an existing booking!", 409);
        }

        Reservation reservation = new Reservation();
        reservation.setSeating(seatingRepository.getReferenceById(addReservationDto.getSeatingId()));
        reservation.setStartTime(addReservationDto.getStartTime());
        reservation.setEndTime(addReservationDto.getStartTime().plusHours(2));
        reservationRepository.save(reservation);
    }
}
