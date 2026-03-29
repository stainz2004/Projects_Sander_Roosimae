package org.example.backend.repository;

import org.example.backend.entity.SeatingPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingPreferenceRepository extends JpaRepository<SeatingPreference, Long> {
}
