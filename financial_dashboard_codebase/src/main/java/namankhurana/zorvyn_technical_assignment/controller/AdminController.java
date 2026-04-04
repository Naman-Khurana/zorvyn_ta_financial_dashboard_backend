package namankhurana.zorvyn_technical_assignment.controller;

import jakarta.validation.Valid;
import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.service.AuthService;
import namankhurana.zorvyn_technical_assignment.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private  final FinancialRecordService financialRecordService;
    private final AuthService authService;

    @Autowired
    public AdminController(FinancialRecordService financialRecordService, AuthService authService) {
        this.financialRecordService = financialRecordService;
        this.authService = authService;
    }


    @PostMapping("/records")
    public ResponseEntity<FinancialRecordDTO> createRecord(@RequestBody CreateFinancialRecordDTO dto) {

        FinancialRecordDTO record = financialRecordService.createRecord(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PutMapping("/records/{recordId}")
    public ResponseEntity<FinancialRecordDTO> updateRecord(@PathVariable Long recordId, @RequestBody
    FinancialRecordRequestDTO financialRecordRequestDTO) {

        return ResponseEntity.ok().body(financialRecordService.updateRecord(financialRecordRequestDTO, recordId));

    }

    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long recordId) {

        financialRecordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO userDetails) {

        authService.registerUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User Created Successfully.");
    }



}
