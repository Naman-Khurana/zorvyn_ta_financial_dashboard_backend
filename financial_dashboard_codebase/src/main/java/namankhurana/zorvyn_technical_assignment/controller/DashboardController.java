package namankhurana.zorvyn_technical_assignment.controller;

import namankhurana.zorvyn_technical_assignment.dto.CategoryWiseRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.DashboardSummaryDTO;
import namankhurana.zorvyn_technical_assignment.enums.TrendTypeEnum;
import namankhurana.zorvyn_technical_assignment.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

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
            @RequestParam(required = false) Long n

    ) {
        return ResponseEntity.ok()
                .body(dashboardService.getRecentNActivities(n));
    }

    @GetMapping("/trends")
    public ResponseEntity<?> trends(
            @RequestParam(required = false) TrendTypeEnum type,
            @RequestParam(required = false) Integer n
    )
    {
        return ResponseEntity.ok()
                .body(dashboardService.getTrends(type,n));
    }


}
