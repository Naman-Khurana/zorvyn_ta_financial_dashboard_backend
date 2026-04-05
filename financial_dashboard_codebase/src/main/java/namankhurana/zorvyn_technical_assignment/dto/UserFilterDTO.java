package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;
import namankhurana.zorvyn_technical_assignment.entity.Role;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDTO {
    private String email;
    private Boolean active;
    private RolesEnum role;
    private LocalDateTime minCreationDate;
    private LocalDateTime maxCreationDate;
    private Long minId;
    private Long maxId;
}
