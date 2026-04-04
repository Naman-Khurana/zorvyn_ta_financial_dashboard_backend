package namankhurana.zorvyn_technical_assignment.specification;


import jakarta.persistence.criteria.Predicate;
import namankhurana.zorvyn_technical_assignment.dto.UserFilterDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class UserSpecification {

    public static Specification<User> filterUsers(
            UserFilterDTO filter
    ) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (filter.getMinId() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("id"), filter.getMinId()));

            if (filter.getMaxId() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("id"), filter.getMaxId()));

            if (filter.getEmail() != null)
                predicates.add(criteriaBuilder.equal(root.get("email"),
                        "%" + filter.getEmail().toLowerCase() + "%"));

            if (filter.getRole() != null)
                predicates.add(criteriaBuilder.equal(root.get("role"), filter.getRole().getName()));

            if (filter.getMinCreationDate() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),
                        filter.getMinCreationDate()));

            if (filter.getMaxCreationDate() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),
                        filter.getMaxCreationDate()));

            if (filter.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), filter.getActive()));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}

