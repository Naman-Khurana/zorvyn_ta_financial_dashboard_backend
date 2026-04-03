package namankhurana.zorvyn_technical_assignment.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
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


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FinancialRecord> financialRecords;


    //convenience Bi-Directional methods

    //for FinancialRecords
    public void addFinancialRecord(FinancialRecord newFinanacialRecord){
        if(financialRecords==null)
            financialRecords=new ArrayList<>();
        // add to user
        financialRecords.add(newFinanacialRecord);

        // add to financial record
        newFinanacialRecord.setUser(this);
    }


}

