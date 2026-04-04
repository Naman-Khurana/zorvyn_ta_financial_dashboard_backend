package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.CategoryEnum;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordFilterDTO {

    private LocalDate startDate;
    private LocalDate endDate;
    private CategoryEnum category;
    private RecordTypeEnum type;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
}