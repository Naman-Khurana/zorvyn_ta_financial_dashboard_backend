package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrendsDTO {
    private String period;
    private BigDecimal income;
    private BigDecimal expense;
}
