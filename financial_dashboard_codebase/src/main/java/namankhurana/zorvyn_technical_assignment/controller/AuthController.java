package namankhurana.zorvyn_technical_assignment.controller;

import jakarta.validation.Valid;
import namankhurana.zorvyn_technical_assignment.dto.LoginDTO;
import namankhurana.zorvyn_technical_assignment.dto.RegisterUserDTO;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;
import namankhurana.zorvyn_technical_assignment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO userDetails) {
        authService.registerUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User Created Successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws Exception {
        UserDTO user = authService.loginUser(loginDTO);
        return ResponseEntity.ok()
                .body(user);
    }
}
