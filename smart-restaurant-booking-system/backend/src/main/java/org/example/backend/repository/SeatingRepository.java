package org.example.backend.repository;

import org.example.backend.entity.Seating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatingRepository extends JpaRepository<Seating, Long>, JpaSpecificationExecutor<Seating> {

    List<Seating> findByReservations_StartTimeLessThanEqualAndReservations_EndTimeGreaterThanEqual(
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}
