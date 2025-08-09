package pl.xsware.domain.model.auth;

import lombok.Builder;
import lombok.Data;
import pl.xsware.domain.model.user.UserDto;

@Data
@Builder
public class LoginResponse {

    private String token;
    private UserDto user;

    public static LoginResponse create(UserDto user, String jwtToken) {
        return LoginResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }
}
