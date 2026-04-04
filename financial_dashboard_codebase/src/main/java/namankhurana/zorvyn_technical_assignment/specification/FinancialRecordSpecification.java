package namankhurana.zorvyn_technical_assignment.specification;

import jakarta.persistence.criteria.Predicate;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordFilterDTO;
import namankhurana.zorvyn_technical_assignment.entity.FinancialRecord;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FinancialRecordSpecification {

    public static Specification<FinancialRecord> filterRecords(
           FinancialRecordFilterDTO filter
    ) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (filter.getStartDate() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("recordDate"), filter.getStartDate()));

            if (filter.getEndDate() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("recordDate"), filter.getEndDate() ));

            if (filter.getCategory() != null)
                predicates.add(criteriaBuilder.equal(root.get("category"), filter.getCategory()));

            if (filter.getType() != null)
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));

            if (filter.getMinAmount() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));

            if (filter.getMaxAmount() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
