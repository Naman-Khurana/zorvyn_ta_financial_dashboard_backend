package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.CategoryWiseRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.DashboardSummaryDTO;
import namankhurana.zorvyn_technical_assignment.dto.TrendsDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.enums.TrendTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {
    DashboardSummaryDTO getTotalIncome();

    DashboardSummaryDTO getTotalExpenses();

    DashboardSummaryDTO getNetBalance();

    List<CategoryWiseRecordDTO> getCategoryWiseTotal();

    List<FinancialRecordDTO> getRecentNActivities(Long limit);


    List<TrendsDTO> getTrends(TrendTypeEnum type, Integer maxLimit);
}
