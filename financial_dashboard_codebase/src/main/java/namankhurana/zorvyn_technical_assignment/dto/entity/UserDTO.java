package namankhurana.zorvyn_technical_assignment.dto.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import namankhurana.zorvyn_technical_assignment.entity.Role;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Boolean active;
    private Role role;
    private LocalDateTime createdAt;
}

