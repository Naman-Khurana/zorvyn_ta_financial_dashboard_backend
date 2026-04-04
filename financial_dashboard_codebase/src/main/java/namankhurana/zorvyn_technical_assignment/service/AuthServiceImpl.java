package namankhurana.zorvyn_technical_assignment.service;

import jakarta.transaction.Transactional;
import namankhurana.zorvyn_technical_assignment.dto.LoginDTO;
import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.entity.Role;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;
import namankhurana.zorvyn_technical_assignment.exception.BadRequestException;
import namankhurana.zorvyn_technical_assignment.exception.EmailAlreadyExistsException;
import namankhurana.zorvyn_technical_assignment.exception.ForbiddenResourceException;
import namankhurana.zorvyn_technical_assignment.exception.UserNotFoundException;
import namankhurana.zorvyn_technical_assignment.mapper.UserMapper;
import namankhurana.zorvyn_technical_assignment.repository.RoleRepository;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, UserService userService, RoleRepository roleRepository, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.authorizationService = authorizationService;
    }


    @Override
    @Transactional
    public UserDTO createUser(RegisterUserDTO registerUserDTO) {

        //access check -> admin only
        User loggedInUser = userService.getLoggedInUser();
        authorizationService.checkAdmin(loggedInUser);

        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        // store email in lowercase
        registerUserDTO.setEmail(registerUserDTO.getEmail().toLowerCase().trim());
        // default role -> Viewer
        RolesEnum roleOrSetDefaultViewer = (registerUserDTO.getRole() == null ? RolesEnum.VIEWER
                : registerUserDTO.getRole());
        Role finalRole = roleRepository.findByName(roleOrSetDefaultViewer)
                .orElseThrow(() -> new BadRequestException("Invalid Role : " + roleOrSetDefaultViewer.getValue()));

        User newUser = userMapper.toUser(registerUserDTO);
        newUser.setRole(finalRole);
        newUser.setActive(true);

        userRepository.save(newUser);
        return userMapper.toDto(newUser);


    }

    @Override
    @Transactional
    public UserDTO loginUser(LoginDTO loginDTO) {
        //TODO : add JWT authentication here

        User authenticatedUser = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> {
            throw new UserNotFoundException("User not found with entered email.");
        });

        return userMapper.toDto(authenticatedUser);
    }




}
