package namankhurana.zorvyn_technical_assignment.controller;

import jakarta.validation.constraints.Min;
import namankhurana.zorvyn_technical_assignment.dto.CategoryWiseRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.DashboardSummaryDTO;
import namankhurana.zorvyn_technical_assignment.enums.TrendTypeEnum;
import namankhurana.zorvyn_technical_assignment.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@PreAuthorize("hasRole('VIEWER')")
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/total-income")
    public ResponseEntity<DashboardSummaryDTO> totalIncome() {
        return ResponseEntity.ok()
                .body(dashboardService.getTotalIncome());
    }

    @GetMapping("/total-expense")
    public ResponseEntity<DashboardSummaryDTO> totalExpense() {
        return ResponseEntity.ok()
                .body(dashboardService.getTotalExpenses());
    }

    @GetMapping("/net-balance")
    public ResponseEntity<DashboardSummaryDTO> netBalance() {
        return ResponseEntity.ok()
                .body(dashboardService.getNetBalance());
    }

    @GetMapping("/category-wise")
    public ResponseEntity<List<CategoryWiseRecordDTO>> categoryWiseTotal() {
        return ResponseEntity.ok()
                .body(dashboardService.getCategoryWiseTotal());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<?> recentActivity(
            @RequestParam(required = false) @Min(value = 1,message = "'n' should have value of atleast 1") Long n

    ) {
        return ResponseEntity.ok()
                .body(dashboardService.getRecentNActivities(n));
    }

    @GetMapping("/trends")
    public ResponseEntity<?> trends(
            @RequestParam(required = false) TrendTypeEnum type,
            @RequestParam(required = false) @Min(value = 1,message = "'n' should have value of atleast 1") Integer n
    )
    {
        return ResponseEntity.ok()
                .body(dashboardService.getTrends(type,n));
    }

}
