package pl.xsware.domain.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.xsware.domain.model.user.UserDto;

import java.util.Collection;

@AllArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDto toDto() {
        return UserDto.builder()
                .id(this.userId)
                .login(this.username)
                .roles(this.authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}
