package namankhurana.zorvyn_technical_assignment.dto;


import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFinancialRecordDTO {

    private Double amount;
    private RecordTypeEnum type;
    private String category;
    private LocalDate date;
    private String notes;
    private String description;
}
