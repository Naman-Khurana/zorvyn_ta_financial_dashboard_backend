package namankhurana.zorvyn_technical_assignment.service;

import jakarta.transaction.Transactional;
import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.entity.FinancialRecord;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;
import namankhurana.zorvyn_technical_assignment.exception.ForbiddenResourceException;
import namankhurana.zorvyn_technical_assignment.exception.ResourceNotFoundException;
import namankhurana.zorvyn_technical_assignment.exception.UserNotFoundException;
import namankhurana.zorvyn_technical_assignment.mapper.FinancialRecordMapper;
import namankhurana.zorvyn_technical_assignment.repository.FinancialRecordRepository;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordMapper financialRecordMapper;
    private final FinancialRecordRepository financialRecordRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Autowired
    public FinancialRecordServiceImpl(FinancialRecordMapper financialRecordMapper, FinancialRecordRepository financialRecordRepository, UserService userService, UserRepository userRepository, AuthService authService) {
        this.financialRecordMapper = financialRecordMapper;
        this.financialRecordRepository = financialRecordRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public FinancialRecordDTO createRecord(CreateFinancialRecordDTO createFinancialRecordDTO) {

        User user = userService.getLoggedInUser();
        FinancialRecord record = financialRecordMapper.toEntity(createFinancialRecordDTO);
        user.addFinancialRecord(record);
        return financialRecordMapper.toDTO(record);

    }

    @Override
    public FinancialRecordDTO getRecord(long recordId) {

        User user = userService.getLoggedInUser();
        FinancialRecord record = financialRecordRepository.findById(recordId).orElseThrow(() -> new ResourceNotFoundException("Record Not Found for ID : " + recordId));
        return financialRecordMapper.toDTO(record);

    }


    // get all records for logged in user
    @Override
    public List<FinancialRecordDTO> getAllRecordsForAUser() {

        Long userId= userService.getCurrentUserId();
        return getAllRecordsForAUser(userId);

    }

    // get all records for user with ID : userId
    // ACCESS Granted-> ADMIN /User itself
    @Override
    public List<FinancialRecordDTO> getAllRecordsForAUser(Long userId) {

        if(userRepository.existsById(userId))
            throw new UserNotFoundException("User not found for ID : " + userId);
        User loggedInUser = userService.getLoggedInUser();

        // check ADMIN or user itself
        authService.checkOwnerOrAdmin(loggedInUser,userId);

        List<FinancialRecord> financialRecords = financialRecordRepository.findByUserId(loggedInUser.getId());
        return financialRecords.stream().map(financialRecordMapper::toDTO).toList();

    }



    //admin only
    @Override
    public List<FinancialRecordDTO> getAllRecords() {

        User user = userService.getLoggedInUser();
        authService.checkAdmin(user);
        List<FinancialRecord> financialRecords = financialRecordRepository.findAll();
        return financialRecords.stream().map(financialRecordMapper::toDTO).toList();

    }


    @Override
    @Transactional
    public FinancialRecordDTO updateRecord(FinancialRecordRequestDTO requestDTO, long recordId){

        User user = userService.getLoggedInUser();
        FinancialRecord record= financialRecordRepository.findById(recordId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Record not found for ID : " + recordId);
        });

        authService.checkOwnerOrAdmin(user,record.getId());

        financialRecordMapper.updateFinancialRecordFromDTO(requestDTO,record);

        return financialRecordMapper.toDTO(record);

    }

    @Transactional
    @Override
    public void deleteRecord(long recordId){
        User user= userService.getLoggedInUser();
        FinancialRecord record=financialRecordRepository.findById(recordId).orElseThrow(()->{
            throw new ResourceNotFoundException("Record not found for ID : " + recordId);
        });

        authService.checkOwnerOrAdmin(user,record.getId());

        financialRecordRepository.delete(record);

    }


}
