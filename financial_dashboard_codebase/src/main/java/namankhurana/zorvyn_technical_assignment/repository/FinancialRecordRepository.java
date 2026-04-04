package namankhurana.zorvyn_technical_assignment.repository;

import namankhurana.zorvyn_technical_assignment.dto.CategoryWiseRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.TrendsDTO;
import namankhurana.zorvyn_technical_assignment.entity.FinancialRecord;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialRecordRepository extends
        JpaRepository<FinancialRecord, Long>
        , JpaSpecificationExecutor<FinancialRecord> {
    List<FinancialRecord> findByUserId(Long userId);

    @Query("""
            SELECT COALESCE(SUM(r.amount), 0)
            FROM FinancialRecord r
            WHERE (:type IS NULL OR r.type = :type)
            AND r.deleted = false
            """)
    BigDecimal getTotalByType(@Param("type") RecordTypeEnum type);


    @Query("""
            SELECT new namankhurana.zorvyn_technical_assignment.dto.CategoryWiseRecordDTO(
                r.category,
                SUM(r.amount)
            )
            FROM FinancialRecord r
            WHERE r.deleted = false
            GROUP BY r.category
            """)
    List<CategoryWiseRecordDTO> getCategorySummary();

    @Query("""
            SELECT new namankhurana.zorvyn_technical_assignment.dto.TrendDTO(
                CONCAT(YEAR(r.recordDate), '-', MONTH(r.recordDate)),
                SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE 0 END),
                SUM(CASE WHEN r.type = 'EXPENSE' THEN r.amount ELSE 0 END)
            )
            FROM FinancialRecord r
            WHERE r.deleted = false
            GROUP BY YEAR(r.recordDate), MONTH(r.recordDate)
            ORDER BY YEAR(r.recordDate) DESC, MONTH(r.recordDate) DESC
            """)
    List<TrendsDTO> getMonthlyTrends(Pageable pageable);

    @Query("""
            SELECT new namankhurana.zorvyn_technical_assignment.dto.TrendDTO(
                CONCAT(YEAR(r.recordDate), '-W', WEEK(r.recordDate)),
                SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE 0 END),
                SUM(CASE WHEN r.type = 'EXPENSE' THEN r.amount ELSE 0 END)
            )
            FROM FinancialRecord r
            WHERE r.deleted = false
            GROUP BY YEAR(r.recordDate), WEEK(r.recordDate)
            ORDER BY YEAR(r.recordDate) DESC, WEEK(r.recordDate) DESC
            """)
    List<TrendsDTO> getWeeklyTrends(Pageable pageable);
}
