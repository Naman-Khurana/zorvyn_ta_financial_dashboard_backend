package namankhurana.zorvyn_technical_assignment.mapper;

import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);
}