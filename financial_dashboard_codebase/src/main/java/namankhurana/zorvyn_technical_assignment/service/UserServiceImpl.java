package namankhurana.zorvyn_technical_assignment.service;

import namankhurana.zorvyn_technical_assignment.dto.UpdateUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.UserFilterDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.exception.UserNotFoundException;
import namankhurana.zorvyn_technical_assignment.mapper.UserMapper;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import namankhurana.zorvyn_technical_assignment.security.UserPrincipal;
import namankhurana.zorvyn_technical_assignment.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorizationService authorizationService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
        this.userMapper = userMapper;
    }

    @Override
    public User getUserFromId(Long userId){
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found for ID : " + userId));
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

    @Override
    public UserDTO updateUser(UpdateUserDTO updateUserDTO, Long userID){
        // allow only admin
        User loggedInUser = getLoggedInUser();
        authorizationService.checkAdmin(loggedInUser);

        User userToUpdate=getUserFromId(userID);


        userMapper.updateUserFromDTO(updateUserDTO,userToUpdate);

        return userMapper.toDto(userToUpdate);

    }

    @Override
    public List<UserDTO> getFilteredUsers(UserFilterDTO userFilterDTO){
        Specification<User> spec= UserSpecification.filterUsers(userFilterDTO);

        return userRepository.findAll(spec)
                .stream()
                .map(userMapper::toDto)
                .toList();


    }

    @Override
    public Page<UserDTO> getFilteredUsersPaged(UserFilterDTO userFilterDTO, Pageable pageable){
        Specification<User> spec= UserSpecification.filterUsers(userFilterDTO);

        return userRepository.findAll(spec,pageable)
                .map(userMapper::toDto);


    }

}
