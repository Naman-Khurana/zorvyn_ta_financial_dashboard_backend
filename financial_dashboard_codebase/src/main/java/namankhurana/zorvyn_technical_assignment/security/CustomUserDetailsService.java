package namankhurana.zorvyn_technical_assignment.security;

import jakarta.transaction.Transactional;
import namankhurana.zorvyn_technical_assignment.entity.User;
import namankhurana.zorvyn_technical_assignment.exception.UserNotFoundException;
import namankhurana.zorvyn_technical_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new UserNotFoundException("User not found for email : " + email));
        return UserPrincipal.build(user);
    }

    public UserDetails loadUserById(String id){
        User user=userRepository.findById(Long.parseLong(id)).orElseThrow(()->
                new UserNotFoundException("User not found with ID : " + id ));

        return UserPrincipal.build(user);
    }
}
