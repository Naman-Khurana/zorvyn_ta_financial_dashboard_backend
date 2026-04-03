package namankhurana.zorvyn_technical_assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDTO {
    private String email;
    private String password;
}