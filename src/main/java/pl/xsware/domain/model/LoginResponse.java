package pl.xsware.domain.model;

import lombok.Builder;
import lombok.Data;
import pl.xsware.domain.model.user.UserDto;

@Data
@Builder
public class LoginResponse {

    private String token;
    private String email;
    private String firstName;
    private String lastName;

    public static LoginResponse create(UserDto user, String jwtToken) {
        return LoginResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
