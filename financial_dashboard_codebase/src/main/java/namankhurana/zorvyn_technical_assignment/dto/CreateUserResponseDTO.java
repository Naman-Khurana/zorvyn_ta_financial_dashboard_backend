package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;
import namankhurana.zorvyn_technical_assignment.dto.entity.UserDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponseDTO {
    String response;
    UserDTO user;
}
