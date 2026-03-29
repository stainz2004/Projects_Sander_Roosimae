package org.example.backend.repository;

import org.example.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsBySeatingIdAndStartTimeBetween(
            Long seatingId,
            LocalDateTime start,
            LocalDateTime end
    );
}
