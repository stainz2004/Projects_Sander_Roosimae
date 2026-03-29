package com.example.veebilehekala.repository;
import com.example.veebilehekala.entity.ShopItem;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;


import java.util.ArrayList;
import java.util.List;
public class ShopItemSpecifications {

    private ShopItemSpecifications() {
        // sonarqube fixing but it doesnt fix
    }
    public static Specification<ShopItem> withFilters(
            Double min,
            Double max,
            List<Integer> categoryIds
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (min != null) {
                predicates.add(cb.ge(root.get("price"), min));
            }

            if (max != null) {
                predicates.add(cb.le(root.get("price"), max));
            }

            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(
                    root.get("category").get("id").in(categoryIds)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
