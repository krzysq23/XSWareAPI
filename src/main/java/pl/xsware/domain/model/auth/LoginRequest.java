package pl.xsware.domain.model.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String login;
    private String email;
    private String password;
}
