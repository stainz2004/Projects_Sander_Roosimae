package org.example.backend.specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.example.backend.entity.Reservation;
import org.example.backend.entity.Seating;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SeatingSpecification {

    private SeatingSpecification () {

    }

    public static Specification<Seating> matchesFilter(LocalDateTime dateAndTime, int numberOfPeople, Long seatingTypeId, Long seatingPreferenceId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (numberOfPeople > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxPeople"), numberOfPeople));
            }

            if (seatingTypeId != null) {
                predicates.add(criteriaBuilder.equal(root.get("seatingType").get("id"), seatingTypeId));
            }

            if (seatingPreferenceId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.join("preferences").get("id"), seatingPreferenceId
                ));
            }

            if (dateAndTime != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Reservation> reservationRoot = subquery.from(Reservation.class);
                subquery.select(reservationRoot.get("seating").get("id"))
                        .where(
                                criteriaBuilder.equal(reservationRoot.get("seating").get("id"), root.get("id")),
                                criteriaBuilder.lessThanOrEqualTo(reservationRoot.get("startTime"), dateAndTime),
                                criteriaBuilder.greaterThanOrEqualTo(reservationRoot.get("endTime"), dateAndTime)
                        );
                predicates.add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
