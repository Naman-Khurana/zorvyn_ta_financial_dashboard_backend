package namankhurana.zorvyn_technical_assignment.controller;

import jakarta.validation.Valid;
import namankhurana.zorvyn_technical_assignment.dto.*;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.mapper.UserMapper;
import namankhurana.zorvyn_technical_assignment.service.AuthService;
import namankhurana.zorvyn_technical_assignment.service.FinancialRecordService;
import namankhurana.zorvyn_technical_assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private  final FinancialRecordService financialRecordService;
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AdminController(FinancialRecordService financialRecordService, AuthService authService, UserService userService, UserMapper userMapper) {
        this.financialRecordService = financialRecordService;
        this.authService = authService;
        this.userService = userService;
        this.userMapper = userMapper;
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

        authService.createUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User Created Successfully.");
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO, @PathVariable Long userId){
        return ResponseEntity.ok()
                .body(userService.updateUser(updateUserDTO,userId));
    }

    @GetMapping("/users")
    public ResponseEntity<?> filteredUsers(UserFilterDTO userFilterDTO, @PageableDefault(size = 10, sort = "created_at"
            , direction = Sort.Direction.DESC)Pageable pageable){

        return ResponseEntity.ok()
                .body(userService.getFilteredUsersPaged(userFilterDTO,pageable));

    }

    @GetMapping("/users/{userid}")
    public ResponseEntity<UserDTO> getUserFromID(@PathVariable Long userId){
        return ResponseEntity.ok()
                .body(userMapper.toDto(userService.getUserFromId(userId)));
    }



}
