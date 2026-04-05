package namankhurana.zorvyn_technical_assignment.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;
import namankhurana.zorvyn_technical_assignment.entity.Role;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    @Size(min = 1, message = "Name cannot be empty")
    private String name;


    @Email(message = "Invalid email format")
    private String email;
    private Boolean active;
    private RolesEnum role;
    @Size(min = 8, message = "Password must contain atleast 8 characters")
    private String password;
}
