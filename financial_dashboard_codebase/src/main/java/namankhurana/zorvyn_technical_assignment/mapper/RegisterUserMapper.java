package namankhurana.zorvyn_technical_assignment.mapper;


import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {
    User toUser(RegisterUserDTO registerUserDTO);
}
