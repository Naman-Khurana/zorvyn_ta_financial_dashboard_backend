package namankhurana.zorvyn_technical_assignment.service;

import jakarta.transaction.Transactional;
import namankhurana.zorvyn_technical_assignment.dto.LoginDTO;
import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.Role;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;
import namankhurana.zorvyn_technical_assignment.exception.EmailAlreadyExistsException;
import namankhurana.zorvyn_technical_assignment.exception.ForbiddenResourceException;
import namankhurana.zorvyn_technical_assignment.exception.UserNotFoundException;
import namankhurana.zorvyn_technical_assignment.mapper.RegisterUserMapper;
import namankhurana.zorvyn_technical_assignment.mapper.UserMapper;
import namankhurana.zorvyn_technical_assignment.repository.RoleRepository;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    private final RegisterUserMapper registerUserMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthServiceImpl(RegisterUserMapper registerUserMapper, UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository) {
        this.registerUserMapper = registerUserMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }






    @Override
    @Transactional
    public User registerUser(RegisterUserDTO registerUserDTO) {

        if(userRepository.existsByEmail(registerUserDTO.getEmail())){
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User newUser = registerUserMapper.toUser(registerUserDTO);
        newUser.setActive(true);

        Role defaultRole = roleRepository.findByName(RolesEnum.VIEWER)
                .orElseThrow(() -> new RuntimeException("Role not configured"));

        newUser.setRole(defaultRole);
        userRepository.save(newUser);

        return null;


    }

    @Override
    @Transactional
    public UserDTO loginUser(LoginDTO loginDTO) {
        //TODO : add JWT authentication here

        User authenticatedUser = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()-> {
            throw new UserNotFoundException("User not found with entered email.");
        });

        return userMapper.toDto(authenticatedUser);
    }

    @Override
    public void checkAdmin(User user) {
        if (user.getRole().getName() != RolesEnum.ADMIN) {
            throw new ForbiddenResourceException("Admin access required");
        }
    }



    @Override
    // if not admin and user itself ->  forbidden
    public void checkOwnerOrAdmin(User currentUser, Long ownerId) {
        boolean isAdmin = currentUser.getRole().getName() == RolesEnum.ADMIN;
        boolean isOwner = Objects.equals(ownerId, currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenResourceException("User not allowed to access this resource");
        }
    }



}
