package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class LoginDTO {
    private String email;
    private String password;
}