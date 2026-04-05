package namankhurana.zorvyn_technical_assignment.service;

import jakarta.transaction.Transactional;
import namankhurana.zorvyn_technical_assignment.dto.UpdateUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.UserFilterDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.Role;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.exception.ResourceNotFoundException;
import namankhurana.zorvyn_technical_assignment.exception.UserNotFoundException;
import namankhurana.zorvyn_technical_assignment.mapper.UserMapper;
import namankhurana.zorvyn_technical_assignment.repository.RoleRepository;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import namankhurana.zorvyn_technical_assignment.security.UserPrincipal;
import namankhurana.zorvyn_technical_assignment.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorizationService authorizationService, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUser(UpdateUserDTO updateUserDTO, Long userID){

        User userToUpdate=getUserFromId(userID);
        if(updateUserDTO.getRole()!=null){
            Role newRole =roleRepository.findByName(updateUserDTO.getRole()).orElseThrow(() ->
                    new ResourceNotFoundException("Entered role doesn't exist"));
            userToUpdate.setRole(newRole);
        }
        if(updateUserDTO.getPassword()!=null){
            updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        userToUpdate=userMapper.updateUserFromDTO(updateUserDTO,userToUpdate);
        // manually mapping rolesEnum to Role
        if(updateUserDTO.getRole()!=null){
            Role newRole =roleRepository.findByName(updateUserDTO.getRole()).orElseThrow(() ->
                    new ResourceNotFoundException("Entered role doesn't exist"));
            userToUpdate.setRole(newRole);
        }
        userRepository.save(userToUpdate);

        return userMapper.toDto(userToUpdate);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void deleteUser(Long userId){
        if(!userRepository.existsById(userId))
            throw new UserNotFoundException("User not found  for ID : " + userId );
        userRepository.deleteById(userId);


    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getFilteredUsers(UserFilterDTO userFilterDTO){
        Specification<User> spec= UserSpecification.filterUsers(userFilterDTO);

        return userRepository.findAll(spec)
                .stream()
                .map(userMapper::toDto)
                .toList();


    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDTO> getFilteredUsersPaged(UserFilterDTO userFilterDTO, Pageable pageable){
        Specification<User> spec= UserSpecification.filterUsers(userFilterDTO);

        return userRepository.findAll(spec,pageable)
                .map(userMapper::toDto);


    }

}
