package namankhurana.zorvyn_technical_assignment.repository;

import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord,Long> {
    List<FinancialRecord> findByUserId(Long userId);

}
