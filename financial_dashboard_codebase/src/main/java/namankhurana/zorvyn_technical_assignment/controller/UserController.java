package namankhurana.zorvyn_technical_assignment.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('VIEWER')")
@RequestMapping("/api/user")
public class UserController {

}
