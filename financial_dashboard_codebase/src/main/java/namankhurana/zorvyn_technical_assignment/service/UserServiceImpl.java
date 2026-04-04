package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.exception.UserNotFoundException;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import namankhurana.zorvyn_technical_assignment.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }

    @Override
    public User getLoggedInUser() {
        return userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

//    public UserDTO createUser(){
//
//    }
}
