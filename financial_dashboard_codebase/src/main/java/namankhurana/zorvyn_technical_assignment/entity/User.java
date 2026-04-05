package namankhurana.zorvyn_technical_assignment.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User  {

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

    @Builder.Default
    private Boolean active=true;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;



}

