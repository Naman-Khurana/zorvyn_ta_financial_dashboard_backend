package namankhurana.zorvyn_technical_assignment.repository;

import namankhurana.zorvyn_technical_assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByActive(Boolean active);

}
