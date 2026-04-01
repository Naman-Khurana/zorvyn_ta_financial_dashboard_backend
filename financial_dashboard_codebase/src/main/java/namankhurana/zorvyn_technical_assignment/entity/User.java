package namankhurana.zorvyn_technical_assignment.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "name",nullable = false)
    private String name;


    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}

