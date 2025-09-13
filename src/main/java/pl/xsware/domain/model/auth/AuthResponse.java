package pl.xsware.domain.model.auth;

import lombok.Builder;
import lombok.Data;
import pl.xsware.domain.model.user.UserDto;

@Data
@Builder
public class AuthResponse {

    private String accessToken;
    private UserDto user;

}
