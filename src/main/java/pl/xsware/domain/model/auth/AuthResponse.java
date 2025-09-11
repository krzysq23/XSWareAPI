package pl.xsware.domain.model.auth;

import lombok.Builder;
import lombok.Data;
import pl.xsware.domain.model.user.UserDto;

@Data
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private UserDto user;

    public static AuthResponse create(UserDto user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }
}
