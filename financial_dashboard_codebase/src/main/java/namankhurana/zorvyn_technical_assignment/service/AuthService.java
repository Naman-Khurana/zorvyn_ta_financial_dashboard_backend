package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.LoginDTO;
import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;

public interface AuthService {
    public UserDTO createUser(RegisterUserDTO registerUserDTO);

    public UserDTO loginUser(LoginDTO loginDTO);



}
