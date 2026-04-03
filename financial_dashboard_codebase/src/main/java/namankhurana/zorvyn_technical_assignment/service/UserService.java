package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;

public interface UserService {

    public Long getCurrentUserId();
    public User getLoggedInUser();

}
