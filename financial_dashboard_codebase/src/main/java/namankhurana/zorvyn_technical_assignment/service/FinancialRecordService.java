package namankhurana.zorvyn_technical_assignment.service;

import jakarta.transaction.Transactional;
import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;

import java.util.List;

public interface FinancialRecordService {

    FinancialRecordDTO createRecord(CreateFinancialRecordDTO createFinancialRecordDTO);

    FinancialRecordDTO getRecord(long id);

    List<FinancialRecordDTO> getAllRecordsForAUser();

    List<FinancialRecordDTO> getAllRecordsForAUser(Long userId);

    List<FinancialRecordDTO> getAllRecords();

    FinancialRecordDTO updateRecord(FinancialRecordRequestDTO requestDTO, long recordId);

    void deleteRecord(long recordId);
}
