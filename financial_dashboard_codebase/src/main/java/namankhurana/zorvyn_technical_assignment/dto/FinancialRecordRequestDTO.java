package namankhurana.zorvyn_technical_assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
@Getter
@Setter
public class FinancialRecordRequestDTO {
    private BigDecimal amount;
    private RecordTypeEnum type;
    private String category;
    private String description;
    private LocalDate recordDate;
}
