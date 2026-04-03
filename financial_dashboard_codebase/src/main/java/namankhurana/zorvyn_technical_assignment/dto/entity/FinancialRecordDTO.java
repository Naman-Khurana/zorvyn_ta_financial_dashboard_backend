package namankhurana.zorvyn_technical_assignment.dto.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FinancialRecordDTO {
    private Long id;
    private BigDecimal amount;
    private RecordTypeEnum type;
    private String category;
    private String description;
    private LocalDate recordDate;
    private LocalDateTime createdAt;
}
