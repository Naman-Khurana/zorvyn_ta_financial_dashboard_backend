package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.LoginDTO;
import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;

public interface AuthService {
    public UserDTO registerUser(RegisterUserDTO registerUserDTO);

    public UserDTO loginUser(LoginDTO loginDTO);

    void checkAdmin(User user);

    void checkAnalystOrAdmin(User currentUser, Long ownerId);


}
