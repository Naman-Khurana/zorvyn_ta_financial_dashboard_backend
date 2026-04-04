package namankhurana.zorvyn_technical_assignment.service;

import jakarta.transaction.Transactional;
import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordFilterDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.entity.FinancialRecord;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.exception.ResourceNotFoundException;
import namankhurana.zorvyn_technical_assignment.mapper.FinancialRecordMapper;
import namankhurana.zorvyn_technical_assignment.repository.FinancialRecordRepository;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import namankhurana.zorvyn_technical_assignment.specification.FinancialRecordSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordMapper financialRecordMapper;
    private final FinancialRecordRepository financialRecordRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;

    @Autowired
    public FinancialRecordServiceImpl(FinancialRecordMapper financialRecordMapper, FinancialRecordRepository financialRecordRepository, UserService userService, UserRepository userRepository, AuthService authService, AuthorizationService authorizationService) {
        this.financialRecordMapper = financialRecordMapper;
        this.financialRecordRepository = financialRecordRepository;
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @Override

    public FinancialRecordDTO createRecord(CreateFinancialRecordDTO createFinancialRecordDTO) {

        User user = userService.getLoggedInUser();
        authorizationService.checkAdmin(user);

        // convert to Entity ->  store in db -> return DTO
        FinancialRecord record = financialRecordMapper.toEntity(createFinancialRecordDTO);
        financialRecordRepository.save(record);
        return financialRecordMapper.toDTO(record);

    }

    @Override
    public FinancialRecordDTO getRecord(Long recordId) {


        User user = userService.getLoggedInUser();
        FinancialRecord record = financialRecordRepository.findById(recordId).orElseThrow(() -> new ResourceNotFoundException("Record Not Found for ID : " + recordId));
        return financialRecordMapper.toDTO(record);

    }



    @Override
    public List<FinancialRecordDTO> getAllRecords() {

        List<FinancialRecord> financialRecords = financialRecordRepository.findAll();
        return financialRecords.stream().map(financialRecordMapper::toDTO).toList();

    }


    @Override
    @Transactional
    public FinancialRecordDTO updateRecord(FinancialRecordRequestDTO requestDTO, long recordId) {

        User user = userService.getLoggedInUser();
        authorizationService.checkAdmin(user);

        FinancialRecord record = financialRecordRepository.findById(recordId).orElseThrow(() ->
                new ResourceNotFoundException("Record not found for ID : " + recordId)
        );

        financialRecordMapper.updateFinancialRecordFromDTO(requestDTO, record);
        return financialRecordMapper.toDTO(record);

    }

    @Transactional
    @Override
    public void deleteRecord(Long recordId) {
        User user = userService.getLoggedInUser();
        authorizationService.checkAdmin(user);

        if (financialRecordRepository.existsById(recordId)) {
            throw new ResourceNotFoundException("Record not found for ID : " + recordId);
        }

        financialRecordRepository.deleteById(recordId);

    }

    // get list object
    @Override
    public List<FinancialRecordDTO> getFilteredRecords(FinancialRecordFilterDTO financialRecordFilterDTO) {


        Specification<FinancialRecord> spec = FinancialRecordSpecification.filterRecords(
                financialRecordFilterDTO
        );


        return financialRecordRepository
                .findAll(spec)
                .stream()
                .map(financialRecordMapper::toDTO)
                .toList();


    }

    //get page object
    @Override
    public Page<FinancialRecordDTO> getFilteredRecordsPage(FinancialRecordFilterDTO financialRecordFilterDTO,
                                                           Pageable pageable) {


        Specification<FinancialRecord> spec = FinancialRecordSpecification.filterRecords(
                financialRecordFilterDTO
        );

        // find List<Financial Records> and convert to List<FinancialRecordsDTO>
        return financialRecordRepository
                .findAll(spec, pageable)
                .map(financialRecordMapper::toDTO);


    }


}
