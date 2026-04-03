package namankhurana.zorvyn_technical_assignment.entity;

import jakarta.persistence.*;
import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {


    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RolesEnum name;

}
