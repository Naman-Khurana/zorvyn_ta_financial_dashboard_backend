package namankhurana.zorvyn_technical_assignment.controller;

import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordFilterDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @Autowired
    public FinancialRecordController(FinancialRecordService financialRecordService) {
        this.financialRecordService = financialRecordService;
    }




    @GetMapping("/{recordId}")
    public ResponseEntity<FinancialRecordDTO> getRecord(@PathVariable Long recordId) {
        return ResponseEntity.ok()
                .body(financialRecordService.getRecord(recordId));
    }


    @GetMapping
    public ResponseEntity<?> filteredRecords(FinancialRecordFilterDTO filter,
                                             @PageableDefault(size = 10, sort = "recordDate"
                                             , direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ResponseEntity.ok()
                .body(financialRecordService
                .getFilteredRecordsPage(filter, pageable));

    }

}
