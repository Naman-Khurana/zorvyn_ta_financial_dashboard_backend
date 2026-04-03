package namankhurana.zorvyn_technical_assignment.repository;

import namankhurana.zorvyn_technical_assignment.entity.Role;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(RolesEnum name);
}

