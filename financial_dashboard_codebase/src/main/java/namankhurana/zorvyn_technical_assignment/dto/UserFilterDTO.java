package namankhurana.zorvyn_technical_assignment.dto;

import lombok.*;
import namankhurana.zorvyn_technical_assignment.entity.Role;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDTO {
    private String email;
    private Boolean active;
    private Role role;
    private LocalDateTime minCreationDate;
    private LocalDateTime maxCreationDate;
    private Long minId;
    private Long maxId;
}
