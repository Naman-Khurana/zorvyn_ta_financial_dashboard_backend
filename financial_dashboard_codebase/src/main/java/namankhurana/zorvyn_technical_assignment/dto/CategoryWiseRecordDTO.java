package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.CategoryEnum;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CategoryWiseRecordDTO {
    private CategoryEnum category;
    private RecordTypeEnum type;
    private BigDecimal totalAmount;
}

