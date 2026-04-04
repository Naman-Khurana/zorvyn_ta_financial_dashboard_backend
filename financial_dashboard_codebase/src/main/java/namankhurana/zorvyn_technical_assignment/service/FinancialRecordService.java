package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordFilterDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinancialRecordService {

    FinancialRecordDTO createRecord(CreateFinancialRecordDTO createFinancialRecordDTO);

    FinancialRecordDTO getRecord(Long id);

    List<FinancialRecordDTO> getAllRecords();

    FinancialRecordDTO updateRecord(FinancialRecordRequestDTO requestDTO, long recordId);

    void deleteRecord(Long recordId);

    List<FinancialRecordDTO> getFilteredRecords(FinancialRecordFilterDTO financialRecordFilterDTO);

    Page<FinancialRecordDTO> getFilteredRecordsPage(FinancialRecordFilterDTO financialRecordFilterDTO,
                                                    Pageable pageable);

}
