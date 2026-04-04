package namankhurana.zorvyn_technical_assignment.mapper;

import namankhurana.zorvyn_technical_assignment.dto.CreateFinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.dto.FinancialRecordRequestDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.FinancialRecordDTO;
import namankhurana.zorvyn_technical_assignment.entity.FinancialRecord;
import org.mapstruct.*;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface FinancialRecordMapper {
    FinancialRecord toEntity(CreateFinancialRecordDTO dto);

    FinancialRecordDTO toDTO(FinancialRecord entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFinancialRecordFromDTO(FinancialRecordRequestDTO requestDTO,@MappingTarget FinancialRecord entity);

}
