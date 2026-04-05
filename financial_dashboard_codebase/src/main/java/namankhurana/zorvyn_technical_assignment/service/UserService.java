package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.UpdateUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.UserFilterDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {

    User getUserFromId(Long userId);

    public Long getCurrentUserId();
    public User getLoggedInUser();

    UserDTO updateUser(UpdateUserDTO updateUserDTO, Long userID);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteUser(Long userId);

    List<UserDTO> getFilteredUsers(UserFilterDTO userFilterDTO);

    Page<UserDTO> getFilteredUsersPaged(UserFilterDTO userFilterDTO, Pageable pageable);
}
