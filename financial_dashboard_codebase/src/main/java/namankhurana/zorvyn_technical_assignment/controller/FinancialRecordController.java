package namankhurana.zorvyn_technical_assignment.controller;

import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @Autowired
    public FinancialRecordController(FinancialRecordService financialRecordService) {
        this.financialRecordService = financialRecordService;
    }


    @PostMapping("/")
    public ResponseEntity<FinancialRecordDTO> createRecord(@RequestBody CreateFinancialRecordDTO dto) {

        FinancialRecordDTO record = financialRecordService.createRecord(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<FinancialRecordDTO> getRecord(@PathVariable Long recordId) {
        return ResponseEntity.ok()
                .body(financialRecordService.getRecord(recordId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FinancialRecordDTO>> getAllRecordsForUser(@PathVariable Long userId){
        return ResponseEntity.ok()
                .body(financialRecordService.getAllRecordsForAUser());
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<FinancialRecordDTO> updateRecord(@PathVariable Long recordId, @RequestBody
                FinancialRecordRequestDTO financialRecordRequestDTO){

        return ResponseEntity.ok().body(financialRecordService.updateRecord(financialRecordRequestDTO,recordId));

    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long recordId ){

        financialRecordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }
}
