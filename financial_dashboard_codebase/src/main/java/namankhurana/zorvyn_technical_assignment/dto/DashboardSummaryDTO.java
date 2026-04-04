package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.DashboardSummaryTypeEnum;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {
    DashboardSummaryTypeEnum summaryType;
    BigDecimal value;
}
