package namankhurana.zorvyn_technical_assignment.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.CategoryEnum;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class FinancialRecordRequestDTO {

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount cannot be less than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    private RecordTypeEnum type;

    private CategoryEnum category;

    private String description;

    @PastOrPresent(message = "Record date cannot be in the future")
    private LocalDate recordDate;

    @AssertTrue(message = "Category does not belong to selected type")
    public boolean isCategoryTypeValidation() {
        if (category == null || type == null) return true;
        return category.getType() == type;
    }
}
