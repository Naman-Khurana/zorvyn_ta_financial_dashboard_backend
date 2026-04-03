package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.LoginDTO;
import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;

public interface AuthService {
    public User registerUser(RegisterUserDTO registerUserDTO);

    public UserDTO loginUser(LoginDTO loginDTO);

    void checkAdmin(User user);

    void checkOwnerOrAdmin(User currentUser, Long ownerId);
}
