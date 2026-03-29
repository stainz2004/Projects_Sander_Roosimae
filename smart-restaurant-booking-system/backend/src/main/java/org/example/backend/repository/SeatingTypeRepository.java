package org.example.backend.repository;

import org.example.backend.entity.SeatingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingTypeRepository extends JpaRepository<SeatingType, Long> {
}
