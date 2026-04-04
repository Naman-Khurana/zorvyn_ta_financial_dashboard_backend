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

    @Autowired
    public AuthServiceImpl( UserRepository userRepository, UserMapper userMapper, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {

        //access check
        User loggedInUser = userService.getLoggedInUser();
        checkAdmin(loggedInUser);

        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        // default role -> Viewer
        RolesEnum finalRole = (registerUserDTO.getRole() == null ? RolesEnum.VIEWER : registerUserDTO.getRole());

        User newUser = userMapper.toUser(registerUserDTO);
        newUser.setActive(true);

        Role defaultRole = roleRepository.findByName(finalRole)
                .orElseThrow(() -> new BadRequestException("Invalid Role : " + finalRole.getValue()));

        newUser.setRole(defaultRole);
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

    @Override
    public void checkAdmin(User user) {
        if (user.getRole().getName() != RolesEnum.ADMIN) {
            throw new ForbiddenResourceException("Admin access required");
        }
    }


    @Override
    // if not admin or analyst itself ->  forbidden
    public void checkAnalystOrAdmin(User currentUser, Long ownerId) {
        boolean isAdmin = currentUser.getRole().getName() == RolesEnum.ADMIN;
        boolean isAnalyst = currentUser.getRole().getName() == RolesEnum.ANALYST;

        if (!isAdmin && !isAnalyst) {
            throw new ForbiddenResourceException("User not allowed to access this resource");
        }
    }


}
