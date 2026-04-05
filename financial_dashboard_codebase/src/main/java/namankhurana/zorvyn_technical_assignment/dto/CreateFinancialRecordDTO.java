package namankhurana.zorvyn_technical_assignment.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
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
public class CreateFinancialRecordDTO {


    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount cannot be less than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @NotNull(message = "Record type is required")
    private RecordTypeEnum type;

    @NotNull(message = "Category is required")
    private CategoryEnum category;

    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Record date cannot be in the future")
    private LocalDate recordDate;

    private String description;

    @AssertTrue(message = "Category does not belong to selected type")
    public boolean isCategoryTypeValidation() {
        if (category == null || type == null) return true;
        return category.getType() == type;
    }
}
