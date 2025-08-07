package pl.xsware.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String login;
    private String email;
    private String password;
}
