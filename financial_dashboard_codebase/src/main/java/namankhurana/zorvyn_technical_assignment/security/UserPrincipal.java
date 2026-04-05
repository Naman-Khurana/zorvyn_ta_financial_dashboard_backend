package namankhurana.zorvyn_technical_assignment.security;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import namankhurana.zorvyn_technical_assignment.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    @Getter
    private Long id;
    @Getter
    private String email;
    private String password;
    private GrantedAuthority authority;

    public UserPrincipal(Long id, String email, String password, GrantedAuthority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }


    public static UserPrincipal build(User user){
        GrantedAuthority authority=new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authority
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

}
